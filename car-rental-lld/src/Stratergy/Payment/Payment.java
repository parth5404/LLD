package Stratergy.Payment;

import models.Booking;

public interface Payment {
    public boolean pay(Booking booking);
}
