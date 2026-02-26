package models;

import factory.ProductFactory;
import lombok.Getter;
import lombok.Setter;
import observer.IInventoryObserver;
import strategy.IReplenishment;

import java.util.ArrayList;
import java.util.List;
@Getter
public class InventoryM {
    private static InventoryM instance;
    private final List<Warehouse>warehouses;
    private final ProductFactory productFactory;
    @Setter
    private IReplenishment replenishment;
    private List<IInventoryObserver>observers;
    private InventoryM(IReplenishment stratergy){
        warehouses=new ArrayList<>();
        productFactory=new ProductFactory();
        observers=new ArrayList<>();
        replenishment=stratergy;
    }
    public static synchronized InventoryM getInstance(IReplenishment strategy){
        if(instance==null){
            instance=new InventoryM(strategy);
        }
        return instance;
    }
    public void addObserver(IInventoryObserver obv){
        observers.add(obv);
    }
    public void addWarehouse(Warehouse warehouse){
        if(warehouses.contains(warehouse)){
            throw new RuntimeException("Already a warehouse exists");
        }
        warehouses.add(warehouse);
    }
    public void removeWarehouse(Warehouse warehouse) {
        if(!warehouses.contains(warehouse)){
            throw new RuntimeException("No warehouse exists of this id "+warehouse.getId());
        }
        warehouses.remove(warehouse);
    }
    public Product getProductBySku(String sku) {
        for (Warehouse warehouse : warehouses) {
            Product product = warehouse.getProductBySku(sku);
            if (product != null) {
                return product;
            }
        }
        return null;
    }
    public void checkandReplenish(String sku){
        Product p=getProductBySku(sku);
        if(p.getQuantity()< p.getThreshold()){
            notifyObservers(p);
            if(replenishment!=null)replenishment.addProduct(p);
        }
    }
    public void notifyObservers(Product product) {
        for (IInventoryObserver observer : observers) {
            observer.update(product);
        }
    }



}
