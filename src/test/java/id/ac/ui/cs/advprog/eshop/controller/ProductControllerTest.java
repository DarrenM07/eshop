package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testCreateProductPost() throws Exception {
        // Assuming your Product has a "name" field for demonstration purposes.
        mockMvc.perform(post("/product/create")
                        .param("name", "Test Product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        // Capture the Product passed to service.create
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).create(productCaptor.capture());
        Product createdProduct = productCaptor.getValue();
        assertNotNull(createdProduct.getProductId(), "Product id should be generated");
    }

    @Test
    public void testProductListPage() throws Exception {
        Product p1 = new Product();
        p1.setProductId("1");
        Product p2 = new Product();
        p2.setProductId("2");
        List<Product> productList = Arrays.asList(p1, p2);
        when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attribute("products", productList));
    }

    @Test
    public void testEditProductPageFound() throws Exception {
        Product product = new Product();
        product.setProductId("123");
        List<Product> productList = Collections.singletonList(product);
        when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get("/product/edit/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    public void testEditProductPageNotFound() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPost() throws Exception {
        // Simulate editing a product with id "123" and (optionally) a name.
        mockMvc.perform(post("/product/edit")
                        .param("productId", "123")
                        .param("name", "Updated Product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).update(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();
        assertEquals("123", updatedProduct.getProductId());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(post("/product/delete/123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(productService).delete("123");
    }

    @Test
    public void testEditProductPageWithNullProductId() throws Exception {
        // Create a product with a null productId
        Product productWithNullId = new Product();
        productWithNullId.setProductId(null);
        List<Product> productList = Collections.singletonList(productWithNullId);
        when(productService.findAll()).thenReturn(productList);

        // Since no product has the matching id "123", expect a redirect to the list page
        mockMvc.perform(get("/product/edit/123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

}
