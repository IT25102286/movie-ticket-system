package com.se1020.moviebooking.service;

import com.se1020.moviebooking.model.Seat;
import com.se1020.moviebooking.model.StandardSeat;
import com.se1020.moviebooking.model.VIPSeat;
import com.se1020.moviebooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsByScreening(int screeningId) {
        List<Seat> seats = seatRepository.findByScreeningId(screeningId);
        Map<String, Seat> latestBySeatNumber = new LinkedHashMap<>();
        for (Seat seat : seats) {
            Seat current = latestBySeatNumber.get(seat.getSeatNumber());
            if (current == null || seat.getId() > current.getId()) {
                latestBySeatNumber.put(seat.getSeatNumber(), seat);
            }
        }

        List<Seat> normalized = new ArrayList<>(latestBySeatNumber.values());
        normalized.sort(Comparator
                .comparingInt((Seat s) -> getRowIndex(s.getSeatNumber()))
                .thenComparingInt(s -> getColIndex(s.getSeatNumber())));
        return normalized;
    }

    public Map<String, List<Seat>> getSeatRowsByScreening(int screeningId) {
        List<Seat> seats = getSeatsByScreening(screeningId);
        Map<String, List<Seat>> rows = new LinkedHashMap<>();
        for (Seat seat : seats) {
            String rowLabel = getRowLabel(seat.getSeatNumber());
            rows.computeIfAbsent(rowLabel, k -> new ArrayList<>()).add(seat);
        }
        return rows;
    }

    // Also in SeatService
    public double calculateTotalPrice(int screeningId, List<String> selectedSeats, double basePrice) {
        List<Seat> seats = seatRepository.findByScreeningId(screeningId);
        double total = 0;
        for (Seat seat : seats) {
            if (selectedSeats.contains(seat.getSeatNumber())) {
                total += seat.calculatePrice(basePrice); // calls overridden method
            }
        }
        return total;
    }

    // CREATE — generate seats for a screening (rows x cols)
    public void generateSeats(int screeningId, int rows, int cols) {
        List<Seat> seats = new ArrayList<>();
        String[] rowLabels = {"A","B","C","D","E","F","G","H"};

        for (int r = 0; r < rows; r++) {
            for (int c = 1; c <= cols; c++) {
                Seat seat;
                // last 2 rows are VIP, rest are Standard
                if (r >= rows - 2) {
                    seat = new VIPSeat(0, screeningId, rowLabels[r] + c, false);
                } else {
                    seat = new StandardSeat(0, screeningId, rowLabels[r] + c, false);
                }
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }
    // UPDATE — mark a seat as booked or released
    public void updateSeatStatus(int seatId, boolean booked) {
        Seat seat = seatRepository.findById(seatId);
        if (seat != null) {
            seat.setBooked(booked);
            seatRepository.update(seat);
        }
    }

    // UPDATE — book seats by seat number for a screening
    public void bookSeats(int screeningId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByScreeningId(screeningId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seat.setBooked(true);
                seatRepository.update(seat);
            }
        }
    }

    // UPDATE — release seats when booking is cancelled
    public void releaseSeats(int screeningId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByScreeningId(screeningId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seat.setBooked(false);
                seatRepository.update(seat);
            }
        }
    }

    // DELETE — remove all seats for a screening
    public void deleteSeatsByScreening(int screeningId) {
        seatRepository.deleteByScreeningId(screeningId);
    }

    private int getRowIndex(String seatNumber) {
        if (seatNumber == null || seatNumber.isBlank()) return Integer.MAX_VALUE;
        return Character.toUpperCase(seatNumber.charAt(0)) - 'A';
    }

    private int getColIndex(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) return Integer.MAX_VALUE;
        try {
            return Integer.parseInt(seatNumber.substring(1));
        } catch (NumberFormatException ex) {
            return Integer.MAX_VALUE;
        }
    }

    private String getRowLabel(String seatNumber) {
        if (seatNumber == null || seatNumber.isBlank()) return "?";
        return String.valueOf(Character.toUpperCase(seatNumber.charAt(0)));
    }
}