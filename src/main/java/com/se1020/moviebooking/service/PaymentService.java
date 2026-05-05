package com.se1020.moviebooking.service;

import com.se1020.moviebooking.model.CardPayment;
import com.se1020.moviebooking.model.CashPayment;
import com.se1020.moviebooking.model.Payment;
import com.se1020.moviebooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id);
    }

    public Payment getPaymentByBooking(int bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public List<Payment> getPaymentsByUser(int userId) {
        return paymentRepository.findByUserId(userId);
    }

    // PaymentService.java — updated processPayment
    public void processPayment(String method, int bookingId, int userId,
                               double amount, String cardHolder,
                               String maskedCard, String receivedBy) {
        Payment payment;
        String timestamp = LocalDateTime.now().toString();

        if (method.equals("CARD")) {
            payment = new CardPayment(0, bookingId, userId, amount,
                    "COMPLETED", timestamp, cardHolder, maskedCard);
        } else {
            payment = new CashPayment(0, bookingId, userId, amount,
                    "COMPLETED", timestamp, receivedBy);
        }

        // works on parent type regardless of which subclass
        paymentRepository.save(payment);
    }

    public void updatePaymentStatus(int id, String status) {
        Payment payment = paymentRepository.findById(id);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.update(payment);
        }
    }

    public void deletePayment(int id) {
        paymentRepository.delete(id);
    }
}