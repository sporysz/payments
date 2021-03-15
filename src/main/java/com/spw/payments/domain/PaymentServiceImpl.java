package com.spw.payments.domain;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.model.PaymentNotFoundException;
import com.spw.payments.domain.port.PaymentRepository;
import com.spw.payments.domain.port.PaymentService;

import java.util.List;

public final class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    public Payment create(Payment payment) {
        return repository.save(payment);
    }

    public void update(long id, Payment payment) throws PaymentNotFoundException {
        if (!repository.update(id, payment)) {
           throw new PaymentNotFoundException(id);
        }
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public Payment get(long id) throws PaymentNotFoundException {
        return repository.get(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    public List<Payment> getAll() {
        return repository.getAll();
    }

}
