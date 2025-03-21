package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.InterfaceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private InterfaceProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.create(product);
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product update(Product product) {
        // Extract productId from the product and call repository update.
        return productRepository.update(product.getProductId(), product);
    }


    @Override
    public void delete(String productId) {
        productRepository.delete(productId);
    }
}