package com.se1020.moviebooking.service;

import com.se1020.moviebooking.model.Booking;
import com.se1020.moviebooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatService seatService;

    public BookingService(BookingRepository bookingRepository, SeatService seatService) {
        this.bookingRepository = bookingRepository;
        this.seatService = seatService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(int id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByUser(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void createBooking(Booking booking) {
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);
        seatService.bookSeats(booking.getScreeningId(), booking.getSeatNumbers());
    }

    public void updateBooking(Booking booking) {
        Booking existing = bookingRepository.findById(booking.getId());
        // release old seats, book new ones
        seatService.releaseSeats(existing.getScreeningId(), existing.getSeatNumbers());
        seatService.bookSeats(booking.getScreeningId(), booking.getSeatNumbers());
        bookingRepository.update(booking);
    }

    public void cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id);
        if (booking != null) {
            seatService.releaseSeats(booking.getScreeningId(), booking.getSeatNumbers());
            booking.setStatus("CANCELLED");
            bookingRepository.update(booking);
        }
    }
}