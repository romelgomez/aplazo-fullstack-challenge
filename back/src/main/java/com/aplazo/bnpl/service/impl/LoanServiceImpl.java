package com.aplazo.bnpl.service.impl;

import com.aplazo.bnpl.model.dto.LoanRequest;
import com.aplazo.bnpl.model.dto.LoanResponse;
import com.aplazo.bnpl.model.dto.PaymentPlan;
import com.aplazo.bnpl.model.dto.InstallmentResponse;
import com.aplazo.bnpl.model.entity.Loan;
import com.aplazo.bnpl.model.enums.InstallmentStatus;
import com.aplazo.bnpl.model.entity.Installment;
import com.aplazo.bnpl.model.entity.Customer;
import com.aplazo.bnpl.repository.LoanRepository;
import com.aplazo.bnpl.repository.CustomerRepository;
import com.aplazo.bnpl.service.LoanService;
import com.aplazo.bnpl.exception.CustomerNotFoundException;
import com.aplazo.bnpl.exception.InsufficientCreditException;
import com.aplazo.bnpl.exception.LoanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

  private final LoanRepository loanRepository;
  private final CustomerRepository customerRepository;
  private static final int NUMBER_OF_INSTALLMENTS = 5;
  private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.05"); // 5% commission

  @Override
  @Transactional
  public LoanResponse createLoan(LoanRequest request) {
    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + request.getCustomerId()));

    if (request.getAmount().compareTo(customer.getAvailableCreditLineAmount()) > 0) {
      throw new InsufficientCreditException(String.format(
          "Insufficient credit available. Available: %s, Requested: %s",
          customer.getAvailableCreditLineAmount(),
          request.getAmount()));
    }

    Loan loan = new Loan();
    loan.setCustomer(customer);
    loan.setAmount(request.getAmount());

    // Calculate installments
    BigDecimal installmentAmount = request.getAmount()
        .divide(BigDecimal.valueOf(NUMBER_OF_INSTALLMENTS), 2, RoundingMode.HALF_UP);

    LocalDate currentDate = LocalDate.now();

    for (int i = 0; i < NUMBER_OF_INSTALLMENTS; i++) {
      Installment installment = new Installment();
      installment.setLoan(loan);
      installment.setAmount(installmentAmount);
      installment.setScheduledPaymentDate(currentDate.plusMonths(i + 1));
      if (i == 0) {
        installment.setStatus(InstallmentStatus.NEXT);
      }
      loan.getInstallments().add(installment);
    }

    // Update customer's available credit
    customer.setAvailableCreditLineAmount(
        customer.getAvailableCreditLineAmount().subtract(request.getAmount()));
    customerRepository.save(customer);

    Loan savedLoan = loanRepository.save(loan);
    return mapToLoanResponse(savedLoan);
  }

  @Override
  @Transactional(readOnly = true)
  public LoanResponse getLoan(UUID loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + loanId));
    return mapToLoanResponse(loan);
  }

  private LoanResponse mapToLoanResponse(Loan loan) {
    LoanResponse response = new LoanResponse();
    response.setId(loan.getId());
    response.setCustomerId(loan.getCustomer().getId());
    response.setAmount(loan.getAmount());
    response.setStatus(loan.getStatus().name());
    response.setCreatedAt(loan.getCreatedAt());

    PaymentPlan paymentPlan = new PaymentPlan();
    paymentPlan.setCommissionAmount(loan.getAmount().multiply(COMMISSION_RATE));

    List<InstallmentResponse> installmentResponses = loan.getInstallments().stream()
        .map(this::mapToInstallmentResponse)
        .toList();
    paymentPlan.setInstallments(installmentResponses);

    response.setPaymentPlan(paymentPlan);
    return response;
  }

  private InstallmentResponse mapToInstallmentResponse(Installment installment) {
    InstallmentResponse response = new InstallmentResponse();
    response.setAmount(installment.getAmount());
    response.setScheduledPaymentDate(installment.getScheduledPaymentDate());
    response.setStatus(installment.getStatus().name());
    return response;
  }
}