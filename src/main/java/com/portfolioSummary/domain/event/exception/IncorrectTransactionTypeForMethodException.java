package com.portfolioSummary.domain.event.exception;

public class IncorrectTransactionTypeForMethodException extends TransactionException {

    public IncorrectTransactionTypeForMethodException(String message) {
        super(message);
    }
}
