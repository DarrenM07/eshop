package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setup() {
        // Hapus assertion yang mengharuskan data kosong
        // Sekarang tes akan bekerja meskipun ada data lama
    }

    @Test
    public void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testCreateProductPost() throws Exception {
        int initialSize = productService.findAll().size(); // Hitung produk sebelum operasi

        mockMvc.perform(post("/product/create")
                        .param("name", "Test Product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        // Verifikasi produk bertambah 1
        List<Product> products = productService.findAll();
        assertEquals(initialSize + 1, products.size(), "Product count should increase by 1");
    }

    @Test
    public void testProductListPage() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attribute("products", productService.findAll()));
    }

    @Test
    public void testEditProductPageFound() throws Exception {
        Product product = new Product();
        product.setProductId("123");
        productService.create(product);

        mockMvc.perform(get("/product/edit/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testEditProductPageNotFound() throws Exception {
        mockMvc.perform(get("/product/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPost() throws Exception {
        Product product = new Product();
        product.setProductId("123");
        product.setProductName("Original Product");
        productService.create(product);

        mockMvc.perform(post("/product/edit")
                        .param("productId", "123")
                        .param("productName", "Updated Product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        // Verify the product is updated
        List<Product> products = productService.findAll();
        assertTrue(products.stream()
                        .anyMatch(p -> p.getProductId().equals("123") &&
                                p.getProductName().equals("Updated Product")),
                "Product should be updated with new name");
    }


    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setProductId("123");
        productService.create(product);

        int initialSize = productService.findAll().size(); // Hitung produk sebelum penghapusan

        mockMvc.perform(post("/product/delete/123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        // Verifikasi jumlah produk berkurang 1
        assertEquals(initialSize - 1, productService.findAll().size(), "Product count should decrease by 1");
    }
}
