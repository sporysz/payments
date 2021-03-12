package com.spw.payments.adapters.api.rest.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Builder
public class PaymentRequest {
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Currency currency;
    @NonNull
    private long userId;
    @NonNull
    private String accountNumber;
}
