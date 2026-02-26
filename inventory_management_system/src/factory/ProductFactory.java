package factory;

import enums.ProductCategory;
import models.Product;

public class ProductFactory {
    public static Product createProduct(ProductCategory category,String sku,String name
    ,int quantity,int threshold,double rate){
        switch (category){
            case ProductCategory.CLOTHS ->{
                return new ClothsProduct(sku,name,quantity,threshold,rate,category);
            }
            case ProductCategory.ELECTRONICS -> {
                return new ElectronicProduct(sku,name,quantity,threshold,rate,category);
            }
        }
        return null;
    }
}
