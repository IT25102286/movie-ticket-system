package com.se1020.moviebooking.model;

public class Screening {

    private int id;
    private int movieId;
    private String date;      // "2025-06-15"
    private String time;      // "19:30"
    private String hall;
    private int totalSeats;
    private double ticketPrice;

    public Screening() {}

    public Screening(int id, int movieId, String date, String time, String hall, int totalSeats, double ticketPrice) {
        this.id = id;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
        this.hall = hall;
        this.totalSeats = totalSeats;
        this.ticketPrice = ticketPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
}