package com.spw.payments.domain.model;

public class PaymentNotFoundException extends RuntimeException {

    public static final String PAYMENT_WITH_ID_S_WAS_NOT_FOUND = "payment with id: %s was not found";

    public PaymentNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public PaymentNotFoundException(long id) {
        super(String.format(PAYMENT_WITH_ID_S_WAS_NOT_FOUND, id));
    }

}