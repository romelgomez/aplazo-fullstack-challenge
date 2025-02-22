package com.aplazo.bnpl.service;

import com.aplazo.bnpl.model.dto.LoanRequest;
import com.aplazo.bnpl.model.dto.LoanResponse;
import java.util.UUID;

public interface LoanService {
    LoanResponse createLoan(LoanRequest request);
    LoanResponse getLoan(UUID loanId);
}