package com.aplazo.bnpl.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InstallmentResponse {
    private BigDecimal amount;
    private LocalDate scheduledPaymentDate;
    private String status;
}