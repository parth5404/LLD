package observer;

import models.Product;

public interface IInventoryObserver {
  void update(Product product);
}
