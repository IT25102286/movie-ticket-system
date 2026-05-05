package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.Screening;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScreeningRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "screenings.json");
    }

    public List<Screening> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Screening>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Screening findById(int id) {
        return findAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Screening> findByMovieId(int movieId) {
        return findAll().stream()
                .filter(s -> s.getMovieId() == movieId)
                .collect(java.util.stream.Collectors.toList());
    }

    public void save(Screening screening) {
        List<Screening> screenings = findAll();
        int maxId = screenings.stream().mapToInt(Screening::getId).max().orElse(0);
        screening.setId(maxId + 1);
        screenings.add(screening);
        writeAll(screenings);
    }

    public void update(Screening updated) {
        List<Screening> screenings = findAll();
        for (int i = 0; i < screenings.size(); i++) {
            if (screenings.get(i).getId() == updated.getId()) {
                screenings.set(i, updated);
                break;
            }
        }
        writeAll(screenings);
    }

    public void delete(int id) {
        List<Screening> screenings = findAll();
        screenings.removeIf(s -> s.getId() == id);
        writeAll(screenings);
    }

    private void writeAll(List<Screening> screenings) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, screenings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}