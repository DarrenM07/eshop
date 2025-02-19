package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // GET /product/create
    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    // POST /product/create
    @Test
    void testCreateProductPost() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("name", "Test Product")
                        .param("price", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).create(productCaptor.capture());

        Product created = productCaptor.getValue();
        assertNotNull(created.getProductId(), "Product ID should be generated");
    }

    // GET /product/list
    @Test
    void testProductListPage() throws Exception {
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        p.setProductId("id1");
        products.add(p);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", products));
    }

    @Test
    void testEditProductPageValidId() throws Exception {
        Product product = new Product();
        product.setProductId("id1");

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product/edit/{id}", "id1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product));
    }

    @Test
    void testEditProductPageNonexistentId() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/product/edit/{id}", "unknownId"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    // ✅ Added: Test when a product exists (branch where "if condition" is TRUE)
    @Test
    void testEditProductPage_ProductExists() throws Exception {
        Product product = new Product();
        product.setProductId("id1");

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product/edit/{id}", "id1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product));

        // Verify that findAll() was called exactly once
        verify(productService, times(1)).findAll();
    }

    // ✅ Added: Test when no product exists (branch where "if condition" is FALSE)
    @Test
    void testEditProductPage_ProductNotFound() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/product/edit/{id}", "unknownId"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        // Verify that findAll() was called exactly once
        verify(productService, times(1)).findAll();
    }

    // POST /product/edit
    @Test
    void testEditProductPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "id1")
                        .param("name", "Updated Product")
                        .param("price", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).update(productCaptor.capture());

        Product updated = productCaptor.getValue();
        assertEquals("id1", updated.getProductId());
    }

    // POST /product/delete/{id} - valid ID
    @Test
    void testDeleteProductValidId() throws Exception {
        mockMvc.perform(post("/product/delete/{id}", "id1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
        verify(productService).delete("id1");
    }
}
