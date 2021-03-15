package com.spw.payments;

import com.spw.payments.domain.model.Payment;
import nl.garvelink.iban.IBAN;

import java.math.BigDecimal;
import java.util.Currency;

public class TestDataProvider {
    public static final String AMOUNT_1_STR = "12.34";
    public static final String AMOUNT_2_STR = "10.34";
    public static final long USER_ID = 0L;
    public static final long WRONG_ID = 1000L;
    public static final String USD_STR = "USD";
    public static final Currency USD = Currency.getInstance(USD_STR);
    public static final BigDecimal AMOUNT_1 = new BigDecimal(AMOUNT_1_STR);
    public static final BigDecimal AMOUNT_2 = new BigDecimal(AMOUNT_2_STR);
    public static final String IBAN_STR = "GB14BARC20035317455188";
    public static final nl.garvelink.iban.IBAN IBAN = nl.garvelink.iban.IBAN.parse(IBAN_STR);
    public static final long INITIAL_PAYMENT_ID = 0l;

    public static Payment preparePayment(BigDecimal amount) {
    return preparePayment(amount, INITIAL_PAYMENT_ID);
    }
    public static Payment preparePayment(BigDecimal amount,long id) {
        Payment payment = Payment.builder()
                .id(id)
                .userId(USER_ID)
                .currency(USD)
                .amount(amount)
                .accountNumber(IBAN)
                .build();
        return payment;
    }
}
