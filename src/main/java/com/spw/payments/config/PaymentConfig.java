package com.spw.payments.config;

import com.spw.payments.domain.PaymentServiceImpl;
import com.spw.payments.domain.port.PaymentService;
import com.spw.payments.domain.port.PaymentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class PaymentConfig {
    @Bean
    PaymentService paymentService(final PaymentRepository repository) {
        return new PaymentServiceImpl(repository);
    }
}
