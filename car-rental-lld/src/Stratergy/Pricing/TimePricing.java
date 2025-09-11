package Stratergy.Pricing;

import models.Vehicle;

public class TimePricing implements Pricing {
    @Override
    public double calcPrice(Vehicle v, int start, int end, double dist) {
        return 5;
    }
}
