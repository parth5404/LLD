package Repo;

import models.Booking;

import java.util.HashMap;
import java.util.Map;

public class BookingRepo {
    private final Map<String, Booking>bookingMap=new HashMap<>();

    public void addBooking(String id, Booking book){
        bookingMap.put(id, book);
    }
    public Booking getBooking(String id){
        return bookingMap.get(id);
    }
}
