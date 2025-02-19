package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setProductId("p1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);

        product2 = new Product();
        product2.setProductId("p2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);
    }

    /** ✅ Test: create() should call repository and return correct product */
    @Test
    void testCreate() {
        when(productRepository.create(product1)).thenReturn(product1);

        Product result = productService.create(product1);

        verify(productRepository, times(1)).create(product1);
        assertEquals(product1, result);
    }

    /** ✅ Test: findAll() should return a list of products */
    @Test
    void testFindAll() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products.iterator());

        List<Product> result = productService.findAll();

        verify(productRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    /** ✅ Test: findAll() should return empty list when no products exist */
    @Test
    void testFindAllEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyIterator());

        List<Product> result = productService.findAll();

        verify(productRepository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }

    /** ✅ Test: update() should return updated product */
    @Test
    void testUpdate() {
        when(productRepository.update(product1)).thenReturn(product1);

        Product result = productService.update(product1);

        verify(productRepository, times(1)).update(product1);
        assertEquals(product1, result);
    }

    /** ✅ Test: update() should return null if product not found */
    @Test
    void testUpdateNotFound() {
        when(productRepository.update(product1)).thenReturn(null);

        Product result = productService.update(product1);

        verify(productRepository, times(1)).update(product1);
        assertNull(result);
    }

    /** ✅ Test: delete() should execute repository delete method */
    @Test
    void testDelete() {
        productService.delete("p1");

        verify(productRepository, times(1)).delete("p1");
    }
}
