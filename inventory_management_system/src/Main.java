import enums.ProductCategory;
import factory.ElectronicProduct;
import factory.ProductFactory;
import models.InventoryM;
import models.Product;
import models.Warehouse;
import observer.AdminObserver;
import strategy.IReplenishment;
import strategy.JustInTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;

public class Main{
    public static void main(String[] args) throws IOException {
        IReplenishment type=new JustInTime();
        ProductFactory productFactory=new ProductFactory();
        ElectronicProduct ep= (ElectronicProduct) productFactory.createProduct(ProductCategory.ELECTRONICS,"123"
        ,"electronics",1,43,3.0);
        Warehouse w1= new Warehouse(1,"w1","pune");
        w1.addProduct(ep,0);
        InventoryM manager=InventoryM.getInstance(type);
        manager.addWarehouse(w1);
        AdminObserver adminObserver=new AdminObserver("sher", List.of("Admin Sher"));
        manager.addObserver(adminObserver);
        manager.checkandReplenish("123");
    }
}