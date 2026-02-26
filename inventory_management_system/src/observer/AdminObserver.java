package observer;

import models.Product;

import java.util.List;

public class AdminObserver implements IInventoryObserver{
    private String alertlvl;
    private List<String> admins;
    private AdminObserver(String alertlvl,List<String> admins){
        this.alertlvl=alertlvl;
        this.admins=admins;
    }
    @Override
    public void update(Product product) {
        double stockPercentage =
                ((double) product.getQuantity() / product.getThreshold()) * 100;

        if (stockPercentage <= 25) {
            // Critical alert - red notification
            System.out.println("CRITICAL ALERT: " + product.getName()
                    + " stock critically low at " + product.getQuantity() + " units ("
                    + String.format("%.1f", stockPercentage) + "% of threshold)");
            notifyAdmins(product, "CRITICAL");
        } else if (stockPercentage <= 50) {
            // Warning alert - yellow notification
            System.out.println("WARNING ALERT: " + product.getName()
                    + " stock low at " + product.getQuantity() + " units ("
                    + String.format("%.1f", stockPercentage) + "% of threshold)");
            notifyAdmins(product, "WARNING");
        }
    }

    private void notifyAdmins(Product product, String warning) {
        for (String admin : admins) {
            System.out.println("Dashboard notification sent to admin: " + admin
                    + " - " + alertlvl + " level alert for " + product.getName());
            // Actual implementation would update dashboard UI and push notifications
        }
    }

}
