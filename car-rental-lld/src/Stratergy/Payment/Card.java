package Stratergy.Payment;

import enums.BookingStatus;
import models.Booking;

public class Card implements Payment{
    @Override
    public boolean pay(Booking booking) {
        System.out.println("Booking via card"+ booking.getAmount());
//        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return true;
    }
}
