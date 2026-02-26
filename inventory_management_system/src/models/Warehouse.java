package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class Warehouse {
    private int id;
    private String name;
    private String location;
    private Map<String,Product> products;

    private void addProduct(Product p,int quantity){
        Product rem=products.getOrDefault(p.getSku(),null);
        if(rem==null){
            products.put(p.getSku(),p);
        }else {
            rem.incrementQuantity(quantity);
            products.put(rem.getSku(), rem);
        }
    }
    public void removeProduct(String sku,int quantity){
        Product rem=products.getOrDefault(sku,null);
        if(rem==null){
            throw new RuntimeException("No Product with this sku "+ sku);
        }
        if(rem.getQuantity()<quantity){
            throw new RuntimeException("Quantity Exception");
        }
        rem.decrementQuantity(quantity);
        if(rem.getQuantity()==0){
            products.remove(sku);
        }
    }
    public int getQuantity(String sku){
        Product rem=products.getOrDefault(sku,null);
        if(rem==null){
            throw new RuntimeException("No Product with this sku "+ sku);
        }
        return rem.getQuantity();
    }
    public Product getProductBySku(String sku){
        return products.getOrDefault(sku,null);
    }
}
