package com.spw.payments.domain.model;

public class PaymentNotFoundException extends Exception {

    public static final String PAYMENT_WITH_ID_S_WAS_NOT_FOUND = "payment with id: %s was not found";

    public PaymentNotFoundException(long id) {
        super(String.format(PAYMENT_WITH_ID_S_WAS_NOT_FOUND, id));
    }

}
