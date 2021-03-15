package com.spw.payments.adapters.repository;

import com.spw.payments.domain.model.Payment;
import com.spw.payments.domain.port.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spw.payments.adapters.repository.CsvPaymentMapper.HEADER;
import static com.spw.payments.adapters.repository.CsvPaymentMapper.ID;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.*;

@Service
@Slf4j
public class PaymentCsvRepository implements PaymentRepository {

    private static final CSVFormat DEFAULT_WITH_HEADER = CSVFormat.DEFAULT.withHeader(HEADER);
    private static final CSVFormat DEFAULT_SKIP_HEADER = DEFAULT_WITH_HEADER.withSkipHeaderRecord();

    private final Path paymentsCsv;
    private final Path tempPaymentsCsv;
    private final CsvPaymentMapper mapper;

    public PaymentCsvRepository(@Value("${adapters.repository.csv.file-path}") String paymentsCsv
            , @Value("${adapters.repository.csv.temp-file-path}") String paymentsCsvTemp, CsvPaymentMapper mapper) {
        this.paymentsCsv = Paths.get(paymentsCsv);
        this.tempPaymentsCsv = Paths.get(paymentsCsvTemp);
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        long id = getNewId(paymentsCsv);
        CSVFormat format = getFormat(paymentsCsv);
        StandardOpenOption[] openOptions = getStandardOpenOption(paymentsCsv);
        try (
                BufferedWriter writer = newBufferedWriter(paymentsCsv, openOptions);
                CSVPrinter csvPrinter = new CSVPrinter(writer, format)
        ) {
            csvPrinter.printRecord(prepareRecord(payment, id));
            csvPrinter.flush();
        } catch (IOException e) {
            handleIOException(e);
        }
        payment.setId(id);
        return payment;
    }

    @Override
    public boolean update(long id, Payment payment) {
        boolean updated = false;
        if (!doesFileExist(paymentsCsv)) {
            return updated;
        }
        try (
                Reader reader = newBufferedReader(paymentsCsv);
                CSVParser csvParser = new CSVParser(reader, DEFAULT_SKIP_HEADER);
                BufferedWriter writer = newBufferedWriter(tempPaymentsCsv, CREATE);
                CSVPrinter csvPrinter = new CSVPrinter(writer, DEFAULT_WITH_HEADER)
        ) {
            updated = update(id, payment, csvParser, csvPrinter);
            csvPrinter.flush();
        } catch (IOException e) {
            handleIOException(e);
        }
        renameAndRemove(tempPaymentsCsv, paymentsCsv);
        return updated;
    }


    @Override
    public void delete(long id) {
        if (!doesFileExist(paymentsCsv)) {
            return;
        }
        try (
                Reader reader = newBufferedReader(paymentsCsv);
                CSVParser csvParser = new CSVParser(reader, DEFAULT_SKIP_HEADER);
                BufferedWriter writer = newBufferedWriter(tempPaymentsCsv, CREATE, TRUNCATE_EXISTING);
                CSVPrinter csvPrinter = new CSVPrinter(writer, DEFAULT_WITH_HEADER)
        ) {
            delete(id, csvParser, csvPrinter);
        } catch (IOException e) {
            handleIOException(e);
        }
        renameAndRemove(tempPaymentsCsv, paymentsCsv);
    }


    @Override
    public Optional<Payment> get(long id) {
        Payment payment = null;
        if (!doesFileExist(paymentsCsv)) {
            return Optional.empty();
        }
        try (
                Reader reader = newBufferedReader(paymentsCsv);
                CSVParser csvParser = new CSVParser(reader, DEFAULT_SKIP_HEADER)
        ) {
            payment = getPayment(id, csvParser);

        } catch (IOException e) {
            handleIOException(e);
        }

        return Optional.ofNullable(payment);
    }

    @Override
    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        if (!doesFileExist(paymentsCsv)) {
            return payments;
        }
        try (
                Reader reader = newBufferedReader(paymentsCsv);
                CSVParser csvParser = new CSVParser(reader, DEFAULT_SKIP_HEADER)
        ) {
            payments = getPayments(csvParser);

        } catch (IOException e) {
            handleIOException(e);
        }

        return payments;
    }

    private List<Payment> getPayments(CSVParser csvParser) throws IOException {
        return csvParser.getRecords()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    private void delete(long id, CSVParser csvParser, CSVPrinter csvPrinter) throws IOException {
        for (CSVRecord record : csvParser) {
            if (recordHasSameId(record, id)) {
                continue;
            }
            csvPrinter.printRecord(record);
        }
    }

    private boolean update(long id, Payment payment, CSVParser csvParser, CSVPrinter csvPrinter) throws IOException {
        boolean updated = false;
        for (CSVRecord record : csvParser) {
            if (recordHasSameId(record, id)) {
                csvPrinter.printRecord(prepareRecord(payment, id));
                payment.setId(id);
                updated = true;
            } else {
                csvPrinter.printRecord(record);
            }
        }
        return updated;
    }

    private void renameAndRemove(Path tempPaymentsCsv, Path paymentsCsv) {
        File updated = tempPaymentsCsv.toFile();
        File old = paymentsCsv.toFile();
        boolean deleted = old.delete();
        boolean renamed = updated.renameTo(old)                           ;
        if (!deleted || !renamed) {
            log.error("Problem With swapping files");
          throw new RuntimeException("Problem With swapping files");
        }
    }
    private boolean recordHasSameId(CSVRecord record, long id) {
        return record.get(ID).equals(String.valueOf(id));
    }

    private List<Serializable> prepareRecord(Payment payment, long id) {
        return Arrays.asList(id, payment.getAmount(), payment.getCurrency(), payment.getUserId(), payment.getAccountNumber());
    }

    private CSVFormat getFormat(Path path) {
        return doesFileExist(path) ? DEFAULT_SKIP_HEADER : DEFAULT_WITH_HEADER;
    }

    private StandardOpenOption[] getStandardOpenOption(Path path) {
        return doesFileExist(path) ? new StandardOpenOption[]{APPEND} : new StandardOpenOption[]{CREATE, TRUNCATE_EXISTING};
    }

    private boolean doesFileExist(Path path) {
        return path.toFile().exists();
    }

    private Payment getPayment(long id, CSVParser csvParser) throws IOException {
        String idString = String.valueOf(id);
        return csvParser.getRecords()
                .stream()
                .filter(r -> idString.equals(r.get(ID)))
                .map(mapper::toDomain)
                .findFirst()
                .orElse(null);
    }

    private long getNewId(Path path) {
        long id = 0;
        if (!doesFileExist(path)) {
            return id;
        }
        try (
                Reader reader = newBufferedReader(path);
                CSVParser csvParser = new CSVParser(reader, DEFAULT_SKIP_HEADER)
        ) {
            id = getNewId(path, csvParser);
        } catch (IOException e) {
            handleIOException(e);
        }
        return id;
    }

    private long getNewId(Path path, CSVParser csvParser) throws IOException {
        List<CSVRecord> records = csvParser.getRecords();
        long id = 0;
        if (doesFileExist(path) && records.size() > 0) {
            CSVRecord record = records.get(records.size() - 1);
            id = Long.parseLong(record.get(0)) + 1;
        }
        return id;
    }

    private void handleIOException(IOException e) {
        log.error("problem with file", e);
        throw new RuntimeException(e);
    }


}
