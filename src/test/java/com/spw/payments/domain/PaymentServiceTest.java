package com.spw.payments.domain;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.port.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
   private  PaymentService underTest;

    @Test
    void testPaymentCreation() {
        //given
        Payment payment = Payment.builder().build();
        //when
        underTest.create(payment);
        //then
        verify(repository, times(1)).save(payment);
    }

    @Test
    void testPaymentUpdate() {
        Payment payment = Payment.builder().build();
        long id = 0;
        //when
        underTest.update(id, payment);
        //then
        verify(repository, times(1)).update(id,payment);
    }

    @Test
    void testPaymentDelete() {
        long id = 0;
        //when
        underTest.delete(id);
        //then
        verify(repository, times(1)).delete(id);
    }

    @Test
    void testGetPaymentById() {
        long id = 0;
        //when
        underTest.get(id);
        //then
        verify(repository, times(1)).get(id);
    }

    @Test
    void testGetAllPaymnets() {

        underTest.getAll();
        //then
        verify(repository, times(1)).getAll();
    }
}