package factory;

import model.*;
import stratergy.payment;

import java.util.List;
import java.util.Objects;

public class ScheduledOrder implements OrderFactory{
    private String scheduledTime;
    public ScheduledOrder(String scheduleTime) {
        this.scheduledTime=scheduleTime;
    }
    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems, payment paymentStrategy, double totalCost, String orderType) {
       Order order=null;
        if(orderType.equals("Parcel")){
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else if (orderType.equals("Pickup")) {
            PickUp pickorder=new PickUp();
            pickorder.setResadd(restaurant.getLoc());
            order=pickorder;
        }
        order.setUser(user);
        order.setRes(restaurant);
        order.settime("now");
        order.setPayment(paymentStrategy);
        return order;
    }
}
