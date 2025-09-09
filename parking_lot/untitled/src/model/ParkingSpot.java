package model;

import Enums.Vtype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    private int id;
    public static int nextid=0;
    private Vtype allowed;
    private AtomicBoolean occupied=new AtomicBoolean(false);

    public ParkingSpot(Vtype vehicle){
        this.id=++nextid;
        this.allowed=vehicle;
    }

    public int getId() {
        return id;
    }
    public boolean tryOccupy(){
        return occupied.compareAndSet(false,true);
    }
    public void vacate(){
         occupied.set(false);
    }
    public boolean isOccupied(){
        return occupied.get();
    }
    public Vtype getAllowed(){
        return allowed;
    }

}
