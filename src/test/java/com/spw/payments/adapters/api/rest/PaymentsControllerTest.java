package com.spw.payments.adapters.api.rest;

import com.spw.payments.domain.model.PaymentNotFoundException;
import com.spw.payments.domain.port.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    @Test
    void testGetPayments() throws Exception {
        when(paymentService.getAll()).thenReturn(emptyList());
        mvc.perform(get("/v1/payments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(EMPTY_ARRAY));
    }

    @Test
    void paymentNotFoundFOrGivenIdTest() throws Exception {
        long id = 1L;
        Mockito.doThrow(new PaymentNotFoundException(id)).when(paymentService).get(id);
        mvc.perform(get("/v1/payments/"+id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.format(PaymentNotFoundException.PAYMENT_WITH_ID_S_WAS_NOT_FOUND,id)));
    }

}