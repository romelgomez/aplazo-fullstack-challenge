package com.aplazo.bnpl.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.aplazo.bnpl.model.enums.InstallmentStatus;

@Data
@Entity
@Table(name = "installments")
public class Installment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_id", nullable = false)
  private Loan loan;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(name = "scheduled_payment_date", nullable = false)
  private LocalDate scheduledPaymentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private InstallmentStatus status = InstallmentStatus.PENDING;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}