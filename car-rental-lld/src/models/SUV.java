package models;

import enums.VehicleStatus;
import enums.VehicleType;

public class SUV extends Vehicle{
    public SUV(VehicleType type, int num, int pricekm, int pricetime){
        super(type,num,pricekm,pricetime);
    }
}
