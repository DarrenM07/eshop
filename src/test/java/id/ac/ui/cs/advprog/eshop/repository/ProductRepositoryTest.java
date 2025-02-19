package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext(), "Expected at least one product in repository");
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext(), "Repository should be empty");
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
        assertTrue(productIterator.hasNext(), "Expected at least one product in repository");
        Product retrievedProduct = productIterator.next();
        assertEquals(firstProduct.getProductId(), retrievedProduct.getProductId());
        retrievedProduct = productIterator.next();
        assertEquals(secondProduct.getProductId(), retrievedProduct.getProductId());
        assertFalse(productIterator.hasNext(), "No more products should be present");
    }

    @Test
    void testUpdateProductPositive() {
        // Create and add a product.
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Update the product with the same ID.
        product.setProductName("Sampo Cap Bambang Updated");
        product.setProductQuantity(120);
        Product result = productRepository.update(product);

        // Verify that the update occurred.
        assertNotNull(result, "Expected the product to be updated");
        assertEquals("Sampo Cap Bambang Updated", result.getProductName());
        assertEquals(120, result.getProductQuantity());
    }

    @Test
    void testUpdateProductNegative() {
        // Attempt to update a non-existent product.
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Fake Product");
        nonExistentProduct.setProductQuantity(999);

        Product result = productRepository.update(nonExistentProduct);
        assertNull(result, "Expected null when updating a non-existent product");
    }

    /**
     * Forces the loop in update(...) to skip the first product and then match the second.
     */
    @Test
    void testUpdateSecondProduct() {
        // Create two products
        Product p1 = new Product();
        p1.setProductId("first-id");
        p1.setProductName("First Product");
        p1.setProductQuantity(10);
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("second-id");
        p2.setProductName("Second Product");
        p2.setProductQuantity(20);
        productRepository.create(p2);

        // Attempt to update the second product
        Product updatedP2 = new Product();
        updatedP2.setProductId("second-id");
        updatedP2.setProductName("Second Product Updated");
        updatedP2.setProductQuantity(99);

        Product result = productRepository.update(updatedP2);
        assertNotNull(result, "Expected to successfully update the second product");
        assertEquals("Second Product Updated", result.getProductName());
        assertEquals(99, result.getProductQuantity());
    }

    @Test
    void testDeleteProductPositive() {
        // Create and add a product.
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Delete the product.
        boolean deleteResult = productRepository.delete("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertTrue(deleteResult, "Expected deletion to succeed");

        // Verify the repository is empty.
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext(), "Repository should be empty after deletion");
    }

    @Test
    void testDeleteProductNegative() {
        // Attempt to delete a non-existent product.
        boolean deleteResult = productRepository.delete("non-existent-id");
        assertFalse(deleteResult, "Expected deletion to fail for a non-existent product");
    }

    /**
     * Forces the loop in delete(...) to skip the first product and then match the second.
     */
    @Test
    void testDeleteSecondProduct() {
        // Create two products
        Product p1 = new Product();
        p1.setProductId("first-id");
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("second-id");
        productRepository.create(p2);

        // Attempt to delete the second product
        boolean deleteResult = productRepository.delete("second-id");
        assertTrue(deleteResult, "Expected to successfully delete the second product");

        // Verify only the first product remains
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext(), "Expected one product remaining");
        Product remaining = productIterator.next();
        assertEquals("first-id", remaining.getProductId());
        assertFalse(productIterator.hasNext(), "No more products should remain");
    }
}
