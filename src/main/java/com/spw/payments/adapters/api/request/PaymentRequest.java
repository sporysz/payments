package com.spw.payments.adapters.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Builder
public class PaymentRequest {
    private BigDecimal amount;
    private Currency currency;
    private long userId;
    private String accountNumber;
}
