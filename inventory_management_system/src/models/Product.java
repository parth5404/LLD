package models;

import enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Product {
    private String sku;
    private String name;
    private int quantity;
    private int threshold;
    private double rate;
    private ProductCategory productCategory;

    public void incrementQuantity(int quantity){
        this.quantity=this.quantity+quantity;
    }
    public void decrementQuantity(int quantity){
        this.quantity=this.quantity-quantity;
    }
}
