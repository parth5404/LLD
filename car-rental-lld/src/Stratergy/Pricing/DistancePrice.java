package Stratergy.Pricing;

import models.Vehicle;

public class DistancePrice implements Pricing {
    @Override
    public double calcPrice(Vehicle v, int start, int end, double dist) {
        double base_amt=5.0;
        switch (v.getType()){
            case SUV -> base_amt+=10;
            case SEDAN -> base_amt+=20;
        }
        return base_amt+ 5*dist;
    }
}
