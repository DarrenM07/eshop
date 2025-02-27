package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.Iterator;

public interface InterfaceProductRepository {
    Product create(Product product);
    Iterator<Product> findAll();
    Product findById(String productId);
    Product update(Product product);
    boolean delete(String productId);
}