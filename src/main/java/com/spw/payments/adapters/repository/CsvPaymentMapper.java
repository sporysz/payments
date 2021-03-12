package com.spw.payments.adapters.repository;

import com.spw.payments.domain.model.Payment;
import nl.garvelink.iban.IBAN;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
public class CsvPaymentMapper {

    public static final String ID = "ID";
    public static final String AMOUNT = "AMOUNT";
    public static final String CURRENCY = "CURRENCY";
    public static final String USER_ID = "USER_ID";
    public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";

    public static final String[] HEADER = {ID, AMOUNT, CURRENCY, USER_ID, ACCOUNT_NUMBER};

    public Payment toDomain(CSVRecord r) {
        return Payment.builder()
                .id(Long.parseLong(r.get(ID)))
                .amount(new BigDecimal(r.get(AMOUNT)))
                .currency(Currency.getInstance(r.get(CURRENCY)))
                .userId(Long.parseLong(r.get(USER_ID)))
                .accountNumber(IBAN.parse(r.get(ACCOUNT_NUMBER)))
                .build();
    }
}
