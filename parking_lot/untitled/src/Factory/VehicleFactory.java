package Factory;

import Enums.Vtype;
import model.*;
public class VehicleFactory {
    public static Vehicle create(Vtype type, int number){
                switch (type) {
                    case CAR:
                        return new Car(number, type);
//                    case BIKE:
//                        return new Bike(number, type);
//                    case TRUCK:
//                        return new Truck(number, type);
//                    case EV_CAR:
//                        return new EVCar(number, type);
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + type);
                }
    }
}
