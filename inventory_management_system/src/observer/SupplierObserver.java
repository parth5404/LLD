package observer;

import models.Product;

public class SupplierObserver implements IInventoryObserver{
    private String supplierName;
    private String contactEmail;

    public SupplierObserver(String supplierName, String contactEmail) {
        this.supplierName = supplierName;
        this.contactEmail = contactEmail;
    }
    @Override
    public void update(Product product) {
        if (product.getQuantity() < product.getThreshold()) {
            // Send email notification to supplier
            System.out.println("Notification sent to " + supplierName
                    + " for low stock of " + product.getName());
        }
    }
}
