package models;

import Stratergy.Payment.Payment;
import enums.BookingStatus;
import enums.PaymentStatus;
import enums.PaymentType;

public class Booking {
    private String bookingId;
    private User user;
    private Vehicle vehicle;
    private Branch pickup;
    private Branch drop;
    private int startTime;
    private int endTime;
    private double amount;
    private BookingStatus status = BookingStatus.CREATED;
    private PaymentStatus paystatus = PaymentStatus.PENDING;
    private PaymentType paymentType;

    // Constructor
    public Booking(String bookingId, User user, Vehicle vehicle, Branch pickup, Branch drop,
                   int startTime, int endTime, double amount,PaymentType type) {
        this.bookingId = bookingId;
        this.user = user;
        this.vehicle = vehicle;
        this.pickup = pickup;
        this.drop = drop;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount = amount;
        this.paymentType=type;
    }

    public void setPaystatus(PaymentStatus paystatus) {
        this.paystatus = paystatus;
    }
    public void setBookingStatus(BookingStatus status){
        this.status=status;
    }
    public BookingStatus getBookingStatus(){
        return this.status;
    }
    public PaymentStatus getPaystatus(){
        return this.paystatus;
    }

    public double getAmount() {
        return amount;
    }
    public PaymentType getPaymentType(){
        return paymentType;
    }
}