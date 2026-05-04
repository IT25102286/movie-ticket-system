// model/StandardSeat.java
package com.se1020.moviebooking.model;

public class StandardSeat extends Seat {

    public StandardSeat() {
        super();
    }

    public StandardSeat(int id, int screeningId, String seatNumber, boolean booked) {
        super(id, screeningId, seatNumber, booked, "STANDARD");
    }

    @Override
    public double getPriceMultiplier() {
        return 1.0;  // base price, no change
    }
}