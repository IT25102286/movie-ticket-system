package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.Payment;
import com.se1020.moviebooking.model.Booking;
import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.service.PaymentService;
import com.se1020.moviebooking.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    // CREATE — show payment page
    @GetMapping("/pay/{bookingId}")
    public String paymentPage(@PathVariable int bookingId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "redirect:/bookings/my?error=notfound";
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && booking.getUserId() != user.getId()) {
            return "redirect:/bookings/my?error=unauthorized";
        }

        model.addAttribute("booking", booking);
        model.addAttribute("payment", new Payment());
        return "payments/pay";
    }

    // CREATE — process payment ,,
    // PaymentController.java — updated pay endpoint
    @PostMapping("/pay")
    public String processPayment(@RequestParam int bookingId,
                                 @RequestParam String method,
                                 @RequestParam(required = false) String cardHolderName,
                                 @RequestParam(required = false) String maskedCardNumber,
                                 @RequestParam(required = false) String receivedBy,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "redirect:/bookings/my?error=notfound";
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && booking.getUserId() != user.getId()) {
            return "redirect:/bookings/my?error=unauthorized";
        }
        if (method == null || method.trim().isEmpty()) {
            return "redirect:/payments/pay/" + bookingId + "?error=method";
        }

        double amount = booking.getTotalAmount();

        paymentService.processPayment(method, bookingId, user.getId(), amount,
                cardHolderName, maskedCardNumber, receivedBy);

        return "redirect:/payments/receipt/" + bookingId;
    }

    // READ — receipt
    @GetMapping("/receipt/{bookingId}")
    public String receipt(@PathVariable int bookingId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Payment payment = paymentService.getPaymentByBooking(bookingId);
        if (payment == null) return "redirect:/payments/my?error=notfound";

        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && payment.getUserId() != user.getId()) {
            return "redirect:/payments/my?error=unauthorized";
        }

        model.addAttribute("payment", payment);
        model.addAttribute("booking", bookingService.getBookingById(bookingId));
        return "payments/receipt";
    }

    // READ — user payment history
    @GetMapping("/my")
    public String myPayments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("payments", paymentService.getPaymentsByUser(user.getId()));
        return "payments/myPayments";
    }

    // UPDATE — update payment status (admin, e.g. mark refunded)
    @PostMapping("/admin/update/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam String status) {
        paymentService.updatePaymentStatus(id, status);
        return "redirect:/payments/admin";
    }

    // DELETE — remove payment record (admin)
    @PostMapping("/admin/delete/{id}")
    public String deletePayment(@PathVariable int id) {
        paymentService.deletePayment(id);
        return "redirect:/payments/admin";
    }

    // Admin — all payments
    @GetMapping("/admin")
    public String adminPayments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments/adminList";
    }
}