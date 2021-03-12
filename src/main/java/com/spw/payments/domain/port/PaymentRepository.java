package com.spw.payments.domain.port;

import com.spw.payments.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment>  update(long id, Payment payment);
    void delete(long id);
    Optional<Payment> get(long id);
    List<Payment> getAll();
}
