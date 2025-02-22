package com.aplazo.bnpl.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentPlan {
    private BigDecimal commissionAmount;
    private List<InstallmentResponse> installments;
}