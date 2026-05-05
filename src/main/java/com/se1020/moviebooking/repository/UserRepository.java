package com.se1020.moviebooking.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se1020.moviebooking.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Value("${data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getFile() {
        return new File(dataPath, "users.json");
    }

    public List<User> findAll() {
        try {
            File file = getFile();
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public User findById(int id) {
        return findAll().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        String normalizedEmail = email.trim();
        return findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().trim().equalsIgnoreCase(normalizedEmail))
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        List<User> users = findAll();
        int maxId = users.stream().mapToInt(User::getId).max().orElse(0);
        user.setId(maxId + 1);
        users.add(user);
        writeAll(users);
    }

    public void update(User updated) {
        List<User> users = findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updated.getId()) {
                users.set(i, updated);
                break;
            }
        }
        writeAll(users);
    }

    public void delete(int id) {
        List<User> users = findAll();
        users.removeIf(u -> u.getId() == id);
        writeAll(users);
    }

    private void writeAll(List<User> users) {
        try {
            File file = getFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}