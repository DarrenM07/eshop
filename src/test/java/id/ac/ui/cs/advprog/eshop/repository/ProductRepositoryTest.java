package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product firstProduct = new Product();
        firstProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        firstProduct.setProductName("Sampo Cap Bambang");
        firstProduct.setProductQuantity(100);
        productRepository.create(firstProduct);

        Product secondProduct = new Product();
        secondProduct.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        secondProduct.setProductName("Sampo Cap Usep");
        secondProduct.setProductQuantity(50);
        productRepository.create(secondProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product retrievedProduct = productIterator.next();
        assertEquals(firstProduct.getProductId(), retrievedProduct.getProductId());
        retrievedProduct = productIterator.next();
        assertEquals(secondProduct.getProductId(), retrievedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProductPositive() {
        // Create and add a product
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Edit product details
        product.setProductName("Sampo Cap Bambang Updated");
        product.setProductQuantity(120);
        productRepository.update(product);

        // Retrieve and verify the updated product
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product updatedProduct = productIterator.next();
        assertEquals("Sampo Cap Bambang Updated", updatedProduct.getProductName());
        assertEquals(120, updatedProduct.getProductQuantity());
    }

    @Test
    void testEditProductNegative() {
        // Attempt to edit a non-existent product
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Fake Product");
        nonExistentProduct.setProductQuantity(999);

        Product result = productRepository.update(nonExistentProduct);
        assertNull(result, "Expected null when updating a non-existent product");
    }

    @Test
    void testDeleteProductPositive() {
        // Create and add a product
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Delete the product
        productRepository.delete("eb558e9f-1c39-460e-8860-71af6af63bd6");

        // Verify the product is deleted
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductNegative() {
        // Attempt to delete a non-existent product
        boolean result = productRepository.delete("non-existent-id");
        assertFalse(result, "Expected false when deleting a non-existent product");
    }
}
