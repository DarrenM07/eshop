package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private InterfaceProductRepository productRepository = new ProductRepository();

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
        // Call update with productId and product.
        Product result = productRepository.update(product.getProductId(), product);

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

        // Call update with both the product ID and the product
        Product result = productRepository.update(nonExistentProduct.getProductId(), nonExistentProduct);
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

        // Pass both the product ID and updated product to the update method
        Product result = productRepository.update(updatedP2.getProductId(), updatedP2);
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
        // Set up repository with one product.
        Product p = new Product();
        p.setProductId("first-id");
        productRepository.create(p);

        // Count products before deletion.
        int countBefore = 0;
        Iterator<Product> iteratorBefore = productRepository.findAll();
        while (iteratorBefore.hasNext()) {
            iteratorBefore.next();
            countBefore++;
        }

        // Attempt to delete a non-existent product.
        productRepository.delete("non-existent-id");

        // Count products after deletion.
        int countAfter = 0;
        Iterator<Product> iteratorAfter = productRepository.findAll();
        while (iteratorAfter.hasNext()) {
            iteratorAfter.next();
            countAfter++;
        }

        // Verify that the repository still contains the same number of products.
        assertEquals(countBefore, countAfter, "Deletion of a non-existent product should not change the repository count");
    }

    @Test
    void testDeleteSecondProduct() {
        // Create two products.
        Product p1 = new Product();
        p1.setProductId("first-id");
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("second-id");
        productRepository.create(p2);

        // Delete the product with id "second-id".
        productRepository.delete("second-id");

        // Verify that "second-id" is no longer in the repository.
        boolean foundSecond = false;
        Iterator<Product> iterator = productRepository.findAll();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals("second-id")) {
                foundSecond = true;
                break;
            }
        }
        assertFalse(foundSecond, "Expected to successfully delete the second product");

        // Verify only the first product remains.
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext(), "Expected one product remaining");
        Product remaining = productIterator.next();
        assertEquals("first-id", remaining.getProductId());
        assertFalse(productIterator.hasNext(), "No more products should remain");
    }

    @Test
    void testFindByIdReturnsNull() {
        // Buat sebuah produk dan simpan ke repository (walaupun method findById tidak menggunakannya)
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        // Karena findById belum diimplementasikan, hasilnya harus selalu null.
        assertNull(productRepository.findById("test-id"),
                "findById harus mengembalikan null karena belum diimplementasikan");
    }

}
