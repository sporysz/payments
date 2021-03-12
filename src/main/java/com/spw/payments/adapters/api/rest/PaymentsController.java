package com.spw.payments.adapters.api.rest;

import com.spw.payments.adapters.api.rest.request.PaymentRequest;
import com.spw.payments.adapters.api.rest.response.PaymentResponse;
import com.spw.payments.domain.PaymentService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(
                mapper.toResponse(
                        paymentService.getAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getSinglePayment(@PathVariable long id) {
        return ResponseEntity.ok(mapper.toResponse(paymentService.get(id)));
    }

    @PostMapping("")
    public ResponseEntity<PaymentResponse> addPayment(@RequestBody @Validated PaymentRequest payment) {
        return ResponseEntity.ok(mapper.toResponse(paymentService.create(mapper.toDomain(payment))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@RequestBody @Validated PaymentRequest payment, @PathVariable long id) {
        return ResponseEntity.ok(mapper.toResponse(paymentService.update(id, mapper.toDomain(payment))));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        paymentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
