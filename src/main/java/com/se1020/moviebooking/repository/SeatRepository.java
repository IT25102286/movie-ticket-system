package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.Seat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SeatRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "seats.json");
    }

    public List<Seat> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Seat>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Seat> findByScreeningId(int screeningId) {
        return findAll().stream()
                .filter(s -> s.getScreeningId() == screeningId)
                .collect(Collectors.toList());
    }

    public Seat findById(int id) {
        return findAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void saveAll(List<Seat> newSeats) {
        List<Seat> all = findAll();
        int maxId = all.stream().mapToInt(Seat::getId).max().orElse(0);
        for (Seat seat : newSeats) {
            seat.setId(++maxId);
            all.add(seat);
        }
        writeAll(all);
    }

    public void update(Seat updated) {
        List<Seat> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) {
                all.set(i, updated);
                break;
            }
        }
        writeAll(all);
    }

    public void deleteByScreeningId(int screeningId) {
        List<Seat> all = findAll();
        all.removeIf(s -> s.getScreeningId() == screeningId);
        writeAll(all);
    }

    private void writeAll(List<Seat> seats) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, seats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}