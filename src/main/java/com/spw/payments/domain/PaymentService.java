package com.spw.payments.domain;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.port.PaymentRepository;

import java.util.List;

public final class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public Payment create(Payment payment) {
        return repository.save(payment);
    }

    public Payment update(long id, Payment payment) {
        return repository.update(id, payment);
    }
    public void delete(long id) {
        repository.delete(id);
    }

    public Payment get(long id) {
        return repository.get(id);
    }

    public List<Payment> getAll() {
        return repository.getAll();
    }

}
