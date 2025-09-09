package model;

import Enums.Vtype;

import java.util.HashMap;
import java.util.Map;

public class ParkingFloor {
    private int id;
    public static int nextid=0;
    private Map<Integer,ParkingSpot> spot= new HashMap<>();

     public ParkingFloor(){
        this.id=++nextid;
    }
    public int getId(){
        return id;
    }
    public void addSpot(ParkingSpot spoti){
         spot.put(spoti.getId(),spoti);
    }
    public ParkingSpot getParkingSpace(Vtype type){
         for(ParkingSpot spots:spot.values()){
             if(spots.getAllowed()==type && !spots.tryOccupy()){
                 return spots;
             }
         }
         return null;
    }
    public ParkingSpot getspot(int spotid){
         return spot.get(spotid);
    }



}
