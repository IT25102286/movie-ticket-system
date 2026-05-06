package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "bookings.json");
    }

    public List<Booking> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Booking>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Booking findById(int id) {
        return findAll().stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Booking> findByUserId(int userId) {
        return findAll().stream()
                .filter(b -> b.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void save(Booking booking) {
        List<Booking> bookings = findAll();
        int maxId = bookings.stream().mapToInt(Booking::getId).max().orElse(0);
        booking.setId(maxId + 1);
        bookings.add(booking);
        writeAll(bookings);
    }

    public void update(Booking updated) {
        List<Booking> bookings = findAll();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId() == updated.getId()) {
                bookings.set(i, updated);
                break;
            }
        }
        writeAll(bookings);
    }

    public void delete(int id) {
        List<Booking> bookings = findAll();
        bookings.removeIf(b -> b.getId() == id);
        writeAll(bookings);
    }

    private void writeAll(List<Booking> bookings) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, bookings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}