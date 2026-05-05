package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "payments.json");
    }

    public List<Payment> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Payment>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Payment findById(int id) {
        return findAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Payment findByBookingId(int bookingId) {
        return findAll().stream()
                .filter(p -> p.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);
    }

    public List<Payment> findByUserId(int userId) {
        return findAll().stream()
                .filter(p -> p.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void save(Payment payment) {
        List<Payment> payments = findAll();
        int maxId = payments.stream().mapToInt(Payment::getId).max().orElse(0);
        payment.setId(maxId + 1);
        payments.add(payment);
        writeAll(payments);
    }

    public void update(Payment updated) {
        List<Payment> payments = findAll();
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId() == updated.getId()) {
                payments.set(i, updated);
                break;
            }
        }
        writeAll(payments);
    }

    public void delete(int id) {
        List<Payment> payments = findAll();
        payments.removeIf(p -> p.getId() == id);
        writeAll(payments);
    }

    private void writeAll(List<Payment> payments) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, payments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}