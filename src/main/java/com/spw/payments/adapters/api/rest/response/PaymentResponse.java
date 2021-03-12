package com.spw.payments.adapters.api.rest.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Builder
public class PaymentResponse {
    private long id;
    private BigDecimal amount;
    private Currency currency;
    private long userId;
    private String accountNumber;

}
