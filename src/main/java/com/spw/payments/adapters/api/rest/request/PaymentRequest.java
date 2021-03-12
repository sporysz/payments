package com.spw.payments.adapters.api.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Builder
@ApiModel
public class PaymentRequest {

    @NonNull
    @ApiModelProperty(value = "amount of payment",example = "2000.34")
    private BigDecimal amount;

    @NonNull
    @ApiModelProperty(value = "valid currency code",example = "USD")
    private Currency currency;

    @NonNull
    @ApiModelProperty(value = "user identifier")
    private long userId;

    @NonNull
    @ApiModelProperty(value = "target account number in IBAN format ",example = "GB63BARC20040476287465")
    private String accountNumber;
}
