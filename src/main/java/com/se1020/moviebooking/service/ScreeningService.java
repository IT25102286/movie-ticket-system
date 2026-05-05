package com.se1020.moviebooking.service;

import com.se1020.moviebooking.model.Screening;
import com.se1020.moviebooking.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;

    public ScreeningService(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Screening getScreeningById(int id) {
        return screeningRepository.findById(id);
    }

    public List<Screening> getScreeningsByMovie(int movieId) {
        return screeningRepository.findByMovieId(movieId);
    }

    public void addScreening(Screening screening) {
        screeningRepository.save(screening);
    }

    public void updateScreening(Screening screening) {
        screeningRepository.update(screening);
    }

    public void deleteScreening(int id) {
        screeningRepository.delete(id);
    }
}