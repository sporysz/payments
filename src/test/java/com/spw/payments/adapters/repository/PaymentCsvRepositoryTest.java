package com.spw.payments.adapters.repository;

import com.spw.payments.domain.model.Payment;
import nl.garvelink.iban.IBAN;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static com.spw.payments.TestDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentCsvRepositoryTest {

    public static final String TEST_CSV = "./test.csv";
    public static final String TEST_CSV_TMP = "./test.csv.tmp";
    private PaymentCsvRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new PaymentCsvRepository(TEST_CSV, TEST_CSV_TMP, new CsvPaymentMapper());
    }

    @AfterEach
    void tearDown() {
        Path path = Paths.get(TEST_CSV);
        File file = path.toFile();
        if (file.exists()) {
            assertThat(file.delete()).isTrue();
        }
    }

    @Test
    void shouldreturnemptyListIfFileDoesNotExsistEet() {
        //when
        List<Payment> all = underTest.getAll();
        //then
        assertThat(all).isEmpty();
    }

    @Test
    void sholudReturnEmptyOptionalWhileTryingToGetPaymentOfNonExistingId() {
        //given
        long wrongId = WRONG_ID;
        //when
        Optional<Payment> notRetrieved = underTest.get(wrongId);
        //then
        assertThat(notRetrieved).isEmpty();
    }

    @Test
    void sholudReturnFalseWhileTryingToUpdatePaymentOfNonExistingId() {
        //given
        long wrongId = WRONG_ID;
        Payment payment = preparePayment(AMOUNT_1);
        //when
        boolean updated = underTest.update(wrongId, payment);
        //then
        assertThat(updated).isFalse();
    }

    @Test
    void shouldCreatePaymentProperly() {
        //given
        Payment payment = preparePayment(AMOUNT_1);
        //when
        Payment saved = underTest.save(payment);
        //then
        Payment retrieved = underTest.get(saved.getId()).get();
        assertThat(retrieved.getUserId()).isEqualTo(USER_ID);
        assertThat(retrieved.getCurrency()).isEqualTo(USD);
        assertThat(retrieved.getAmount()).isEqualTo(AMOUNT_1);
        assertThat(retrieved.getAccountNumber()).isEqualTo(IBAN);
    }

    @Test
    void shouldUpdateProperly() {
        //given
        Payment payment = preparePayment(AMOUNT_1);
        Payment saved = underTest.save(payment);
        Payment updatePayment = preparePayment(AMOUNT_2);

        //when
        boolean updated = underTest.update(saved.getId(),updatePayment);
        //then
        assertThat(updated).isTrue();
        Payment retrieved = underTest.get(saved.getId()).get();
        assertThat(retrieved.getUserId()).isEqualTo(USER_ID);
        assertThat(retrieved.getCurrency()).isEqualTo(USD);
        assertThat(retrieved.getAmount()).isEqualTo(AMOUNT_2);
        assertThat(retrieved.getAccountNumber()).isEqualTo(IBAN);
    }

    @Test
    void shouldDeleteProperly() {
        //given
        Payment payment1 = preparePayment(AMOUNT_1);
        Payment payment2 = preparePayment(AMOUNT_2);
        Payment saved1 = underTest.save(payment1);
        Payment saved2 = underTest.save(payment2);
        assertThat(underTest.getAll()).hasSize(2);
        assertThat(underTest.get(saved2.getId())).isNotEmpty();
        //when
        underTest.delete(saved2.getId());
        //then
        assertThat(underTest.getAll()).hasSize(1);
        //then
        assertThat(underTest.get(saved1.getId())).isNotEmpty();
        assertThat(underTest.get(saved2.getId())).isEmpty();
    }
}