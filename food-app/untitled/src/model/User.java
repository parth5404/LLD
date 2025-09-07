
package model;
import java.util.Collections;
import java.util.List;

public class User {
    private final int id;
    private final String name;
    private final String address;
    private final Cart cart;

    public User(String name, String address, int id) {
        this.name = name; this.address = address; this.id = id;
        this.cart = new Cart();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    public List<MenuItem> getCartItemsReadOnly() {
        return Collections.unmodifiableList(cart.getItems());
    }

    public void addToCart(MenuItem item) { cart.addItem(item); }
}
