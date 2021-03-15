package com.spw.payments.adapters.api.rest;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.model.PaymentNotFoundException;
import com.spw.payments.domain.port.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.spw.payments.TestDataProvider.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentsController.class)
@AutoConfigureWebClient
@ActiveProfiles("test")
class PaymentsControllerTest {
    public static final String EMPTY_ARRAY = "[]";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    @SpyBean
    private PaymentMapper mapper;

    @Captor
    ArgumentCaptor<Payment> captor;

    @Test
    void testGetPayments() throws Exception {
        when(paymentService.getAll()).thenReturn(emptyList());
        mvc.perform(get("/v1/payments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(EMPTY_ARRAY));
    }

    @Test
    void paymentNotFoundForGivenIdTest() throws Exception {
        long id = 1L;
        Mockito.doThrow(new PaymentNotFoundException(id)).when(paymentService).get(id);
        mvc.perform(get("/v1/payments/"+id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.format(PaymentNotFoundException.PAYMENT_WITH_ID_S_WAS_NOT_FOUND,id)));
    }

    @Test
    void shouldReturnPayment() throws Exception {
        long id = 1L;
        Payment payment = preparePayment(AMOUNT_1,id);
        when(paymentService.get(id)).thenReturn(payment);
        mvc.perform(get("/v1/payments/"+id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amount").value(AMOUNT_1_STR))
                .andExpect(jsonPath("$.currency").value(USD_STR))
                .andExpect(jsonPath("$.accountNumber").value(IBAN.toString()))
                .andExpect(jsonPath("$.userId").value(USER_ID));
    }
    @Test
    void shouldCreatePayment() throws Exception {
        when(paymentService.create(any())).thenReturn(Payment.builder().accountNumber(IBAN).build());
        mvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"accountNumber\": \""+IBAN_STR+"\",\n" +
                        "  \"amount\": "+AMOUNT_1_STR+",\n" +
                        "  \"currency\": \""+USD+"\",\n" +
                        "  \"userId\": "+USER_ID+"\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(paymentService, times(1)).create(captor.capture());
        Payment captured = captor.getValue();
        assertThat(captured.getUserId()).isEqualTo(USER_ID);
        assertThat(captured.getCurrency()).isEqualTo(USD);
        assertThat(captured.getAmount()).isEqualTo(AMOUNT_1);
        assertThat(captured.getAccountNumber()).isEqualTo(IBAN);
    }

    @Test
    void shouldDeletePayment() throws Exception {
        long id = 1L;
        mvc.perform(delete("/v1/payments/"+id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        verify(paymentService, times(1)).delete(id);
    }

}