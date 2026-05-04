// model/VIPSeat.java
package com.se1020.moviebooking.model;

public class VIPSeat extends Seat {

    public VIPSeat() {
        super();
    }

    public VIPSeat(int id, int screeningId, String seatNumber, boolean booked) {
        super(id, screeningId, seatNumber, booked, "VIP");
    }

    @Override
    public double getPriceMultiplier() {
        return 1.75;  // VIP costs 1.75x the base ticket price
    }
}