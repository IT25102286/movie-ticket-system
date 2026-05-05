// model/CashPayment.java
package com.se1020.moviebooking.model;

public class CashPayment extends Payment {

    private String receivedBy; // name of the staff who received cash

    public CashPayment() {
        super();
    }

    public CashPayment(int id, int bookingId, int userId, double amount,
                       String status, String timestamp, String receivedBy) {
        super(id, bookingId, userId, amount, "CASH", status, timestamp);
        this.receivedBy = receivedBy;
    }

    @Override
    public String getPaymentDetails() {
        return "Cash payment received by staff: " + receivedBy;
    }

    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
}