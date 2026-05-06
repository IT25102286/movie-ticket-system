package com.se1020.moviebooking.model;

import java.util.List;

public class Booking {

    private int id;
    private int userId;
    private int screeningId;
    private List<String> seatNumbers;
    private double totalAmount;
    private String status;   // "CONFIRMED" or "CANCELLED"

    public Booking() {}

    public Booking(int id, int userId, int screeningId, List<String> seatNumbers, double totalAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.screeningId = screeningId;
        this.seatNumbers = seatNumbers;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getScreeningId() { return screeningId; }
    public void setScreeningId(int screeningId) { this.screeningId = screeningId; }

    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}