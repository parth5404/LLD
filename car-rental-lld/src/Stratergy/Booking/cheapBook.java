package Stratergy.Booking;

import enums.VehicleStatus;
import models.Vehicle;

import java.util.List;
import java.util.Comparator;

public class cheapBook implements bookingStratergy{
    public Vehicle bookVehicles(List<Vehicle>vehicles){
        List<Vehicle> sortedv = vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getBookingamt))
                .toList();
        for (Vehicle vehicle:sortedv){
            if(vehicle.getIsBooked().compareAndSet(false,true))return vehicle;
        }
        return null;
    }
}