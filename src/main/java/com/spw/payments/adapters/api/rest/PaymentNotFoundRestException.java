package com.spw.payments.adapters.api.rest;

import com.spw.payments.domain.model.PaymentNotFoundException;

public class PaymentNotFoundRestException extends RuntimeException {

    public PaymentNotFoundRestException(PaymentNotFoundException e) {
        super(e.getMessage(),e);
    }
}
