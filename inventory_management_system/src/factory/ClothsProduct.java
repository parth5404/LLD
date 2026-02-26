package factory;

import enums.ProductCategory;
import models.Product;

public class ClothsProduct extends Product {
    public ClothsProduct( String sku, String name, int quantity, int threshold,
                          double rate,ProductCategory category){
        super( sku, name, quantity, threshold, rate, category);
    }
}
