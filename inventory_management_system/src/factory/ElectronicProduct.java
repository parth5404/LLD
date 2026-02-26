package factory;

import enums.ProductCategory;
import models.Product;

public class ElectronicProduct extends Product {
    public ElectronicProduct(String sku, String name, int quantity, int threshold, double rate, ProductCategory productCategory) {
        super(sku, name, quantity, threshold, rate, productCategory);
    }
}
