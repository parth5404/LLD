package factory;

import java.util.List;
import model.*;
import stratergy.*;

public interface OrderFactory {
    Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                      payment paymentStrategy, double totalCost, String orderType);
}