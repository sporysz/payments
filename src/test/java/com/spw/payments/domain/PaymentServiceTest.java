package com.spw.payments.domain;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.model.PaymentNotFoundException;
import com.spw.payments.domain.port.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentServiceImpl underTest;

    @Test
    void shouldThrowExceptionWhengGettingNonExsistingId() {
        //given
        long id = 0;
        when(repository.get(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(PaymentNotFoundException.class, () -> underTest.get(id));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExsistingId() {
        //given
        Payment payment = Payment.builder().build();
        long id = 0;
        when(repository.update(id,payment)).thenReturn(false);
        //when
        //then
        assertThrows(PaymentNotFoundException.class, () -> underTest.update(id, payment));
    }

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
    void testPaymentUpdate() throws PaymentNotFoundException {
        //given
        Payment payment = Payment.builder().build();
        long id = 0;
        when(repository.update(id,payment)).thenReturn(true);
        //when
        underTest.update(id, payment);
        //then
        verify(repository, times(1)).update(id, payment);
    }

    @Test
    void testPaymentDelete() {
        //given
        long id = 0;
        //when
        underTest.delete(id);
        //then
        verify(repository, times(1)).delete(id);
    }

    @Test
    void testGetPaymentById() throws PaymentNotFoundException {
        //given
        long id = 0;
        when(repository.get(id)).thenReturn(Optional.of(Payment.builder().build()));

        //when
        underTest.get(id);
        //then
        verify(repository, times(1)).get(id);
    }

    @Test
    void testGetAllPaymnets() {
        //given
        underTest.getAll();
        //then
        verify(repository, times(1)).getAll();
    }
}