package com.aplazo.bnpl.service;

import com.aplazo.bnpl.model.dto.CustomerRequest;
import com.aplazo.bnpl.model.dto.CustomerResponse;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomer(UUID customerId);
}