package com.spw.payments.domain.port;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.model.PaymentNotFoundException;

import java.util.List;

public interface PaymentService {

    Payment create(Payment payment);

    void update(long id, Payment payment) throws PaymentNotFoundException;

    void delete(long id);

    Payment get(long id) throws PaymentNotFoundException;

    List<Payment> getAll();
}
