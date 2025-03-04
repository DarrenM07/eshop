package id.ac.ui.cs.advprog.eshop.repository;

import enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;
    @BeforeEach
    void setUp(){
        paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("VoucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER",paymentData);
        payments.add(payment);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("deliveryFee", "10");
        paymentData2.put("address", "Petogogan");
        Payment payment2 = new Payment("2", "COD", paymentData2);
        payments.add(payment2);
    }
    @Test
    void testSaveCreate(){
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.getPayment(payments.get(0).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), findResult.getStatus());
        assertEquals(16, findResult.getPaymentData().get("VoucherCode").length());
        assertEquals(payment.getPaymentData().get("VoucherCode"), findResult.getPaymentData().get("VoucherCode"));
    }
    @Test
    void testVoucherInvalid(){
        Map<String, String> invalidPayment = new HashMap<>();
        invalidPayment.put("VoucherCode", "INVALID");
        Payment tempPayment = new Payment("3", "VOUCHER", invalidPayment);
        paymentRepository.save(tempPayment);

        Payment result = paymentRepository.getPayment(tempPayment.getId());
        assertEquals(tempPayment.getId(), result.getId());
        assertEquals(tempPayment.getMethod(), result.getMethod());
        assertEquals(tempPayment.getPaymentData().get("voucherId"), result.getPaymentData().get("voucherId") );
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }
    @Test
    void testCODNoAddress(){
        Map<String, String> invalidPayment = new HashMap<>();
        invalidPayment.put("deliveryFee", "10");
        invalidPayment.put("address", "");
        Payment tempPayment = new Payment("4", "COD", invalidPayment);
        paymentRepository.save(tempPayment);

        Payment result = paymentRepository.getPayment(tempPayment.getId());
        assertEquals(tempPayment.getId(), result.getId());
        assertEquals(tempPayment.getMethod(), result.getMethod());
        assertEquals(tempPayment.getPaymentData().get("address"), result.getPaymentData().get("address") );
        assertEquals(tempPayment.getPaymentData().get("deliveryFee"), result.getPaymentData().get("deliveryFee") );
        assertTrue(result.getPaymentData().get("address").isEmpty());
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }
    @Test
    void testCODNoDeliveryFee(){
        Map<String, String> invalidPayment = new HashMap<>();
        invalidPayment.put("deliveryFee", "");
        invalidPayment.put("address", "Petogogan");
        Payment tempPayment = new Payment("5", "COD", invalidPayment);
        paymentRepository.save(tempPayment);

        Payment result = paymentRepository.getPayment(tempPayment.getId());
        assertEquals(tempPayment.getId(), result.getId());
        assertEquals(tempPayment.getMethod(), result.getMethod());
        assertEquals(tempPayment.getPaymentData().get("address"), result.getPaymentData().get("address") );
        assertEquals(tempPayment.getPaymentData().get("deliveryFee"), result.getPaymentData().get("deliveryFee") );
        assertTrue(result.getPaymentData().get("deliveryFee").isEmpty());
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }
    @Test
    void testFindByIdIfIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.getPayment(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
    }
    @Test
    void testFindByIdIfIdNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.getPayment("zczc");
        assertNull(findResult);
    }
    @Test
    void testFindAll() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List <Payment> payments1 = paymentRepository.getAllPayments();
        assertEquals(2, payments1.size());
    }

    @Test
    void testFindAllEmpty() {
        List <Payment> payments1 = paymentRepository.getAllPayments();
        assertTrue(payments1.isEmpty());
    }
}