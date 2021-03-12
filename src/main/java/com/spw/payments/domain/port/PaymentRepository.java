package com.spw.payments.domain.port;

import com.spw.payments.domain.model.Payment;

import java.util.List;

public interface PaymentRepository {
    Payment save(Payment payment);
    Payment update(long id, Payment payment);
    void delete(long id);
    Payment get(long id);
    List<Payment> getAll();
}
