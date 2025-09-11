package Stratergy.Pricing;

import models.Vehicle;

public interface Pricing {
    double calcPrice(Vehicle v,int start, int end, double dist);
}
