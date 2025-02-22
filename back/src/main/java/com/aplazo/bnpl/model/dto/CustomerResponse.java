package com.aplazo.bnpl.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponse {
  private UUID id;
  private BigDecimal creditLineAmount;
  private BigDecimal availableCreditLineAmount;
  private LocalDateTime createdAt;
}