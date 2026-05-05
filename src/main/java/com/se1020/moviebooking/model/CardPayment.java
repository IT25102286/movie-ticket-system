// model/CardPayment.java
package com.se1020.moviebooking.model;

public class CardPayment extends Payment {

    private String cardHolderName;
    private String maskedCardNumber; // store only last 4 digits e.g. "**** **** **** 4242"

    public CardPayment() {
        super();
    }

    public CardPayment(int id, int bookingId, int userId, double amount,
                       String status, String timestamp,
                       String cardHolderName, String maskedCardNumber) {
        super(id, bookingId, userId, amount, "CARD", status, timestamp);
        this.cardHolderName = cardHolderName;
        this.maskedCardNumber = maskedCardNumber;
    }

    @Override
    public String getPaymentDetails() {
        return "Card payment by " + cardHolderName + " — " + maskedCardNumber;
    }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getMaskedCardNumber() { return maskedCardNumber; }
    public void setMaskedCardNumber(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }
}