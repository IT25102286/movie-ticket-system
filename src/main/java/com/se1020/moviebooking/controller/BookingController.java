package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.Booking;
import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.service.BookingService;
import com.se1020.moviebooking.service.ScreeningService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ScreeningService screeningService;

    public BookingController(BookingService bookingService, ScreeningService screeningService) {
        this.bookingService = bookingService;
        this.screeningService = screeningService;
    }

    // READ — user's bookings
    @GetMapping("/my")
    public String myBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("bookings", bookingService.getBookingsByUser(user.getId()));
        return "bookings/myBookings";
    }

    // READ — booking detail
    @GetMapping("/{id}")
    public String bookingDetail(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Booking booking = bookingService.getBookingById(id);
        if (booking == null) return "redirect:/bookings/my?error=notfound";

        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && booking.getUserId() != user.getId()) {
            return "redirect:/bookings/my?error=unauthorized";
        }

        model.addAttribute("booking", booking);
        model.addAttribute("screening", screeningService.getScreeningById(booking.getScreeningId()));
        return "bookings/detail";
    }

    // CREATE — confirm booking page
    @GetMapping("/confirm")
    public String confirmPage(@RequestParam int screeningId,
                              @RequestParam String seats,
                              HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (seats == null || seats.trim().isEmpty()) {
            return "redirect:/seats/screening/" + screeningId + "?error=noseats";
        }

        double price = screeningService.getScreeningById(screeningId).getTicketPrice();
        String[] seatArray = seats.split(",");
        model.addAttribute("screeningId", screeningId);
        model.addAttribute("seats", seats);
        model.addAttribute("totalAmount", price * seatArray.length);
        model.addAttribute("screening", screeningService.getScreeningById(screeningId));
        return "bookings/confirm";
    }

    // CREATE — submit booking
    @PostMapping("/create")
    public String createBooking(@RequestParam int screeningId,
                                @RequestParam String seats,
                                @RequestParam double totalAmount,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (seats == null || seats.trim().isEmpty()) {
            return "redirect:/seats/screening/" + screeningId + "?error=noseats";
        }
        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setScreeningId(screeningId);
        booking.setSeatNumbers(Arrays.asList(seats.split(",")));
        booking.setTotalAmount(totalAmount);
        bookingService.createBooking(booking);
        return "redirect:/bookings/my";
    }

    // UPDATE — show edit booking
    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable int id, Model model) {
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "bookings/edit";
    }

    // UPDATE — submit edit
    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable int id, @ModelAttribute Booking booking) {
        booking.setId(id);
        bookingService.updateBooking(booking);
        return "redirect:/bookings/my";
    }

    // DELETE — cancel booking
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings/my";
    }

    // Admin — all bookings
    @GetMapping("/admin")
    public String adminAllBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/adminList";
    }
}