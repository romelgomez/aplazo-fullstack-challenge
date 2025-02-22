package com.aplazo.bnpl.service.impl;

import com.aplazo.bnpl.model.dto.CustomerRequest;
import com.aplazo.bnpl.model.dto.CustomerResponse;
import com.aplazo.bnpl.model.entity.Customer;
import com.aplazo.bnpl.repository.CustomerRepository;
import com.aplazo.bnpl.service.CustomerService;
import com.aplazo.bnpl.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private static final BigDecimal DEFAULT_CREDIT_LINE = new BigDecimal("1000.00");

  @Override
  @Transactional
  public CustomerResponse createCustomer(CustomerRequest request) {
    Customer customer = new Customer();
    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setSecondLastName(request.getSecondLastName());
    customer.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));

    // Set default credit line amounts
    customer.setCreditLineAmount(DEFAULT_CREDIT_LINE);
    customer.setAvailableCreditLineAmount(DEFAULT_CREDIT_LINE);

    Customer savedCustomer = customerRepository.save(customer);
    return mapToCustomerResponse(savedCustomer);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponse getCustomer(UUID customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    return mapToCustomerResponse(customer);
  }

  private CustomerResponse mapToCustomerResponse(Customer customer) {
    CustomerResponse response = new CustomerResponse();
    response.setId(customer.getId());
    response.setCreditLineAmount(customer.getCreditLineAmount());
    response.setAvailableCreditLineAmount(customer.getAvailableCreditLineAmount());
    response.setCreatedAt(customer.getCreatedAt());
    return response;
  }
}