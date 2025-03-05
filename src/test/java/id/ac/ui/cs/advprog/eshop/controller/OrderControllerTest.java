package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    public void setup() {
        // Create a dummy product list (assuming Product has a default constructor)
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        // Build a test order with a fixed id and author
        testOrder = Order.builder()
                .id("TEST_ORDER_001")
                .products(products)
                .orderTime(System.currentTimeMillis())
                .author("TestUser")
                .build();
        // Save the test order using OrderService
        orderService.createOrder(testOrder);
    }

    @Test
    public void testShowCreateOrderForm() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void testShowOrderHistoryForm() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    public void testProcessOrderHistory() throws Exception {
        mockMvc.perform(post("/order/history")
                        .param("author", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistoryResult"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "TestUser"));
    }

    @Test
    public void testShowOrderPayPage() throws Exception {
        mockMvc.perform(get("/order/pay/TEST_ORDER_001"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPay"))
                .andExpect(model().attributeExists("order"))
                // Instead of comparing the whole order object, check specific field(s)
                .andExpect(model().attribute("order", org.hamcrest.Matchers.hasProperty("id",
                        org.hamcrest.Matchers.equalTo("TEST_ORDER_001"))));
    }

    @Test
    public void testProcessOrderPay() throws Exception {
        mockMvc.perform(post("/order/pay/TEST_ORDER_001"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(content().string(containsString("PAY-TEST_ORDER_001")));
    }
}
