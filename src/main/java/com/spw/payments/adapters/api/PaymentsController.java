package com.spw.payments.adapters.api;

import com.spw.payments.adapters.api.request.PaymentRequest;
import com.spw.payments.adapters.api.response.PaymentResponse;
import com.spw.payments.domain.PaymentService;
import com.spw.payments.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentService paymentService;
    private final PaymentMapper mapper;

    @GetMapping("/payments")
    public List<PaymentResponse> getPayments() {
        return mapper.toResponse(paymentService.getAll());
    }

    @GetMapping("/payments/{id}")
    public PaymentResponse getSinglePayment(@PathVariable long id) {
        return mapper.toResponse(paymentService.get(id));
    }

    @PostMapping("/payments")
    public PaymentResponse addPayment(@RequestBody PaymentRequest payment) {
        return mapper.toResponse(paymentService.create(mapper.toDomain(payment)));
    }

    @PutMapping("/payments/{id}")
    public PaymentResponse updatePayment(@RequestBody PaymentRequest payment, @PathVariable long id) {
        return mapper.toResponse(paymentService.update(id,mapper.toDomain(payment)));

    }

    @DeleteMapping("/payments/{id}")
    public void deletePost(@PathVariable long id) {
         paymentService.delete(id);

    }
}
