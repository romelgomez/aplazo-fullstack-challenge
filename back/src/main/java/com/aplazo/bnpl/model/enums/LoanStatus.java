package com.aplazo.bnpl.model.enums;

public enum LoanStatus {
    ACTIVE,    // has pending installment payments
    LATE,      // has installment payments with error
    COMPLETED  // all installments are paid
}