package com.peerlender.lendingengine.domain.exception;

public class LoanApplicationNotfoundException extends RuntimeException{

    public LoanApplicationNotfoundException(long loadApplicationId) {
        super("Loan Application with " + loadApplicationId + " not found ");
    }
}
