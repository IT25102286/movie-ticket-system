// model/Payment.java  (parent)
package com.se1020.moviebooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {

    private int id;
    private int bookingId;
    private int userId;
    private double amount;
    private String method;
    private String status;
    private String timestamp;

    public Payment() {}

    public Payment(int id, int bookingId, int userId, double amount,
                   String method, String status, String timestamp) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
    }

    // overridden by children to describe payment specifics
    @JsonIgnore
    public String getPaymentDetails() {
        return "Method: " + method;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}