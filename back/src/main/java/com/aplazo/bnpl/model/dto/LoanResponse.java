package com.aplazo.bnpl.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoanResponse {
    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private PaymentPlan paymentPlan;
}