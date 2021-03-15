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

import static org.assertj.core.api.Assertions.assertThat;

class PaymentCsvRepositoryTest {

    public static final String TEST_CSV = "./test.csv";
    public static final String TEST_CSV_TMP = "./test.csv.tmp";
    public static final long USER_ID = 0L;
    public static final long WRONG_ID = 1000L;
    public static final String USD_STR = "USD";
    public static final Currency USD = Currency.getInstance(USD_STR);
    public static final String AMOUNT_1_STR = "12.34";
    public static final String AMOUNT_2_STR = "10.34";
    public static final BigDecimal AMOUNT_1 = new BigDecimal(AMOUNT_1_STR);
    public static final BigDecimal AMOUNT_2 = new BigDecimal(AMOUNT_2_STR);
    public static final String IBAN_STR = "GB14BARC20035317455188";
    public static final IBAN IBAN = nl.garvelink.iban.IBAN.parse(IBAN_STR);
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

    private Payment preparePayment(BigDecimal amount) {
        Payment payment = Payment.builder()
                .userId(USER_ID)
                .currency(USD)
                .amount(amount)
                .accountNumber(IBAN)
                .build();
        return payment;
    }

}