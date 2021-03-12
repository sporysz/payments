package com.spw.payments.domain.model;

import nl.garvelink.iban.IBAN;

import java.math.BigDecimal;
import java.util.Currency;

public class Payment {

    private long id;
    private BigDecimal amount;
    private Currency currency;
    private long userId;
    private IBAN accountNumber;

    public Payment(long id, BigDecimal amount, Currency currency, long userId, IBAN accountNumber) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.userId = userId;
        this.accountNumber = accountNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setAccountNumber(IBAN accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getUserId() {
        return userId;
    }

    public IBAN getAccountNumber() {
        return accountNumber;
    }

    public static PaymentBuilder builder() {
        return PaymentBuilder.aPayment();
    }

}
