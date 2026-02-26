package strategy;

import models.Product;

public class Bulk implements IReplenishment{
    @Override
    public void addProduct(Product product) {
        System.out.println("Bulk "+product.getSku());
    }
}
