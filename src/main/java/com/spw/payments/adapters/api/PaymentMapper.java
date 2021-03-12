package com.spw.payments.adapters.api;

import com.spw.payments.adapters.api.request.PaymentRequest;
import com.spw.payments.adapters.api.response.PaymentResponse;
import com.spw.payments.domain.model.Payment;
import nl.garvelink.iban.IBAN;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    public Payment toDomain(PaymentRequest payment) {
        return Payment.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .userId(payment.getUserId())
                .accountNumber(IBAN.parse(payment.getAccountNumber()))
                .build();
    }
    public PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .userId(payment.getUserId())
                .accountNumber(payment.getAccountNumber().toString())
                .build();
    }

    public List<PaymentResponse> toResponse(List<Payment> payment) {
        return payment.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
