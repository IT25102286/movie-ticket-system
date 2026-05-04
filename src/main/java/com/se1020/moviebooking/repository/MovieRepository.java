package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "movies.json");
    }

    public List<Movie> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Movie>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Movie findById(int id) {
        return findAll().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Movie movie) {
        List<Movie> movies = findAll();
        int maxId = movies.stream().mapToInt(Movie::getId).max().orElse(0);
        movie.setId(maxId + 1);
        movies.add(movie);
        writeAll(movies);
    }

    public void update(Movie updated) {
        List<Movie> movies = findAll();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == updated.getId()) {
                movies.set(i, updated);
                break;
            }
        }
        writeAll(movies);
    }

    public void delete(int id) {
        List<Movie> movies = findAll();
        movies.removeIf(m -> m.getId() == id);
        writeAll(movies);
    }

    private void writeAll(List<Movie> movies) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}