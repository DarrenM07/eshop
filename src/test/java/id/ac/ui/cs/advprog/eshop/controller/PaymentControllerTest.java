package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    private Payment testPayment;

    @BeforeEach
    public void setup() {
        // Create a dummy order needed to create a payment.
        List<Product> dummyProducts = new ArrayList<>();
        dummyProducts.add(new Product());
        Order dummyOrder = Order.builder()
                .id("ORDER123")
                .products(dummyProducts)
                .orderTime(System.currentTimeMillis())
                .author("DummyUser")
                .status("WAITING_PAYMENT")  // Explicitly set a valid status.
                .build();
        // Create dummy payment data.
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABCD567");
        testPayment = paymentService.addPayment(dummyOrder, "VOUCHER", paymentData);

    }

    // GET /payment/detail
    @Test
    public void testShowPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    // GET /payment/detail/{paymentId}
    @Test
    public void testShowPaymentDetail() throws Exception {
        mockMvc.perform(get("/payment/detail/" + testPayment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailResult"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", hasProperty("id", equalTo(testPayment.getId()))));
    }

    // GET /payment/admin/list
    @Test
    public void testShowPaymentAdminList() throws Exception {
        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    // GET /payment/admin/detail/{paymentId}
    @Test
    public void testShowPaymentAdminDetail() throws Exception {
        mockMvc.perform(get("/payment/admin/detail/" + testPayment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", hasProperty("id", equalTo(testPayment.getId()))));
    }

    // POST /payment/admin/set-status/{paymentId}
    @Test
    public void testSetPaymentStatus() throws Exception {
        // For example, update the payment status to SUCCESS.
        mockMvc.perform(post("/payment/admin/set-status/" + testPayment.getId())
                        .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentStatusResult"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", hasProperty("status", equalTo("SUCCESS"))));
    }
}
