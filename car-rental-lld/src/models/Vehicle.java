package models;

import enums.VehicleStatus;
import enums.VehicleType;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Vehicle {
    private VehicleType type;
    private int num_plate;
    private int pricekm;
    private int pricetime;
    private VehicleStatus status;
    private int bookingcnt=0;
    private double bookingamt=0;
    private AtomicBoolean isBooked=new AtomicBoolean(false);

    Vehicle(VehicleType type, int num, int pricekm, int pricetime){
        this.type=type;
        this.num_plate=num;
        this.pricekm=pricekm;
        this.pricetime=pricetime;
        this.status=VehicleStatus.AVAILABLE;
    }
    public VehicleType getType(){
        return type;
    }
    public void setStatus(VehicleStatus status){
        this.status=status;
    }
    public VehicleStatus getStatus(){
        return this.status;
    }
    public void incrementBooking(){
        this.bookingcnt++;
    }
    public void modifyVehicleStatus(boolean isBooked){
        if(!isBooked)setStatus(VehicleStatus.AVAILABLE);
        else setStatus(VehicleStatus.BOOKED);
    }
    public void setBookingamt(double amt){
        this.bookingamt=amt;
    }
    public double getBookingamt(){
        return bookingamt;
    }

    public AtomicBoolean getIsBooked() {
        return isBooked;
    }
}
