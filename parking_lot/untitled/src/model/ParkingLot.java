package model;

import Enums.PaymentStatus;
import Enums.PaymentType;
import Enums.PriceType;
import Enums.Vtype;
import Factory.PaymentFactory;
import Factory.PricingFactory;
import stratergy.PricingStratergy;
import stratergy.payment.PaymentStratergy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
    private static int nextid=0;
    private int id=0;
    private Map<Integer,ParkingFloor>mp=new HashMap<>();
    private PricingStratergy strategy;
    private Map<Integer,Ticket>ActiveTicket=new HashMap<>();
    public ParkingLot(){
       this.id=++nextid;
    }
    public int getId(){
        return id;
    }
    public void addFloor(ParkingFloor floor){
        mp.put(floor.getId(),floor);
    }
    public Ticket parkVehicle(Vtype Vehicle, LocalDateTime entryTime){
        for(ParkingFloor floor:mp.values()){
            ParkingSpot spot=floor.getParkingSpace(Vehicle);
            if(spot !=null){
                Ticket t1= new Ticket( entryTime,  Vehicle, floor.getId(), spot.getId());
                ActiveTicket.put(t1.getTicketId(),t1);
                return t1;
            }
        }
        return null;
    }
    public void unparkVehicle(Ticket t, LocalDateTime exitTime, PaymentType type, PriceType type1){
        double amount= PricingFactory.create(type1).calcFees(t.getVehicle(),null,t.getEntryTime(),exitTime);
        boolean fees=PaymentFactory.getPaymentStrategy(type).processPayment(t,amount);
        if(fees){
            t.setStatus(PaymentStatus.DONE);
        }
        else{
            t.setStatus(PaymentStatus.PENDING);
            return ;
        }
        mp.get(t.getFloorid()).getspot(t.getSpotid()).vacate();
        ActiveTicket.remove(t.getTicketId(),t);
        System.out.println("Vehicle Removed");
    }
}
