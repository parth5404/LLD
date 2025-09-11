package models;

import enums.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
    private String branchid;
    private String city;
    private String name;
    private final Map<VehicleType, List<Vehicle>> vehicleTypeListMap=new HashMap<>();

    Branch(String id, String city, String name){
        branchid=id;
        this.city=city;
        this.name=name;
    }
    public void addVehicles(Vehicle v){
        List<Vehicle> mp=vehicleTypeListMap.get(v.getType());
        mp.add(v);
        vehicleTypeListMap.put(v.getType(),mp);
    }
    public List<Vehicle>getVehicles(VehicleType v){
        return vehicleTypeListMap.get(v);
    }

}
