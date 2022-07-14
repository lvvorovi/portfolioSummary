package com.portfolioSummary.domain.event.exception;


import com.portfolioSummary.core.validation.ValidationException;

public class TransactionException extends ValidationException {

    public TransactionException(String message) {
        super(message);
    }
}
