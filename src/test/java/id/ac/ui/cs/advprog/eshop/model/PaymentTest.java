package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {
    @Test
    public void testVoucherPaymentDefault() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER",paymentData);
        assertTrue(payment.getPaymentData().containsKey("VoucherCode"));
        assertEquals("1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("VoucherCode"));
        assertEquals("CHECK_PAYMENT", payment.getStatus());
    }
    @Test
    public void testCODPaymentDefault(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("deliveryFee", "10");
        paymentData.put("address", "Petogogan");
        Payment payment = new Payment("2", "COD", paymentData);

        assertTrue(payment.getPaymentData().containsKey("deliveryFee"));
        assertTrue(payment.getPaymentData().containsKey("address"));
        assertEquals("CHECK_PAYMENT", payment.getStatus());
        assertEquals("2", payment.getId());
        assertEquals("COD", payment.getMethod());
        assertEquals("Petogogan", payment.getPaymentData().get("address"));
        assertEquals("10", payment.getPaymentData().get("deliveryFee"));
    }
    @Test
    public void testCreatePaymentSuccessStatus(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }
    @Test
    public void testCreatePaymentInvalidStatus(){
        assertThrows (IllegalArgumentException.class, () -> {
            Map<String, String> paymentData = new HashMap<>();
            paymentData.put("VoucherCode", "ESHOP1234ABC5678");
            Payment payment = new Payment("1", "VOUCHER", "INVALID_STATUS" ,paymentData);
        });
    }
    @Test
    public void testPaymentInvalidStatus(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }
    @Test
    public void testPaymentRejectStatus(){
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    public void testCreatePaymentInvalidMethod() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        // "INVALID_METHOD" is not a valid PaymentMethod, so the Payment constructor
        // should call setMethod, which throws an IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("3", "INVALID_METHOD", paymentData);
        });
    }

}