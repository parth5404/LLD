package strategy;

import models.Product;

public class JustInTime implements IReplenishment{
    @Override
    public void addProduct(Product product) {
        System.out.println("Just In Time Stratergy "+product.getSku());
    }
}
