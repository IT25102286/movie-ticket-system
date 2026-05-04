package com.se1020.moviebooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Seat {

    private int id;
    private int screeningId;
    private String seatNumber;
    private boolean booked;
    private String type; // "STANDARD" or "VIP"

    public Seat() {}

    public Seat(int id, int screeningId, String seatNumber, boolean booked, String type) {
        this.id = id;
        this.screeningId = screeningId;
        this.seatNumber = seatNumber;
        this.booked = booked;
        this.type = type;
    }

    // overridden by children
    @JsonIgnore
    public double getPriceMultiplier() {
        return 1.0;
    }

    public double calculatePrice(double basePrice) {
        return basePrice * getPriceMultiplier();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getScreeningId() { return screeningId; }
    public void setScreeningId(int screeningId) { this.screeningId = screeningId; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}