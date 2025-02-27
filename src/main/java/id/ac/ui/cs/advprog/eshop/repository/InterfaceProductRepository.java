package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.Iterator;

public interface InterfaceProductRepository extends InterfaceRepository<Product, String> {
    // Now the update method becomes:
    // Product update(String productId, Product product);
}
