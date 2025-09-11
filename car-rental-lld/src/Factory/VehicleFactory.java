package Factory;

import enums.VehicleType;
import models.SUV;
import models.Vehicle;

public class VehicleFactory {
    public static Vehicle create(VehicleType type, int num, int pricekm, int pricetime){
        switch (type){
            case SUV -> {
                return new SUV(type,num,pricekm,pricetime);
            }
        }
        return null;
    }
}
