package com.aplazo.bnpl.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String secondLastName;

  @Column(nullable = false)
  private LocalDate dateOfBirth;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal creditLineAmount;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal availableCreditLineAmount;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}