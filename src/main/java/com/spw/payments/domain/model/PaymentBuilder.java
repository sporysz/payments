package com.spw.payments.domain.model;

import nl.garvelink.iban.IBAN;

import java.math.BigDecimal;
import java.util.Currency;

 public final class PaymentBuilder {

    private long id;
    private BigDecimal amount;
    private Currency currency;
    private long userId;
    private IBAN accountNumber;


     private PaymentBuilder() {
    }

    public static PaymentBuilder aPayment() {
        return new PaymentBuilder();
    }

    public PaymentBuilder id(long id) {
        this.id = id;
        return this;
    }

    public PaymentBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public PaymentBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    public PaymentBuilder accountNumber(IBAN accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public Payment build() {
        return new Payment(id, amount, currency, userId, accountNumber);
    }
}
