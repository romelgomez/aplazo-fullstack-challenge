package com.aplazo.bnpl.exception;

public class InvalidCustomerRequestException extends RuntimeException {
    public InvalidCustomerRequestException(String message) {
        super(message);
    }
}