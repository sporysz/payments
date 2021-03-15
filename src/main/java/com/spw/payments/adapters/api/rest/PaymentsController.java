package com.spw.payments.adapters.api.rest;

import com.spw.payments.adapters.api.rest.request.PaymentRequest;
import com.spw.payments.adapters.api.rest.response.PaymentResponse;
import com.spw.payments.domain.model.PaymentNotFoundException;
import com.spw.payments.domain.port.PaymentService;
import com.spw.payments.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentService paymentService;
    private final PaymentMapper mapper;

    @GetMapping("")
    public ResponseEntity<List<PaymentResponse>> getPayments() {
        List<Payment> payments = paymentService.getAll();
        List<PaymentResponse> body = mapper.toResponse(payments);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getSinglePayment(@PathVariable long id) {
        Payment payment;
        try {
            payment = paymentService.get(id);
        } catch (PaymentNotFoundException e) {
            throw new PaymentNotFoundRestException(e);
        }
        PaymentResponse body = mapper.toResponse(payment);
        return ResponseEntity.ok(body);
    }

    @PostMapping("")
    public ResponseEntity<PaymentResponse> addPayment(@RequestBody @Validated PaymentRequest payment) {
        Payment domain = mapper.toDomain(payment);
        PaymentResponse body = mapper.toResponse(paymentService.create(domain));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@RequestBody @Validated PaymentRequest payment, @PathVariable long id) {
        Payment domain = mapper.toDomain(payment);
        try {
            paymentService.update(id, domain);
        } catch (PaymentNotFoundException e) {
            throw new PaymentNotFoundRestException(e);
        }
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable long id) {
        paymentService.delete(id);
        return ResponseEntity.accepted().build();
    }
}
