package id.ac.ui.cs.advprog.eshop.service;

import enums.PaymentStatus;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import enums.PaymentMethod;
import enums.OrderStatus;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    List<Payment> payments;
    List<Order> orders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName ("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orders = new ArrayList<>();
        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);

        Order order2 = new Order ("7f9e15bb-4b15-42f4-aebc-c3af385fb078", products, 1708570000L, "Safira Sudrajat");
        orders.add(order2);

        payments = new ArrayList<>();
        Map<String, String> paymentDataVoucher = new HashMap<>();
        Map<String, String> paymentDataCOD = new HashMap<>();
        paymentDataVoucher.put("VoucherCode", "ESHOP1234ABC5678");
        paymentDataCOD.put("address", "Petogogan");
        paymentDataCOD.put("deliveryFee", "10");
        Payment payment1 = new Payment(order1.getId(), PaymentMethod.COD.getValue(),  paymentDataCOD);
        payments.add(payment1);
        Payment payment2 = new Payment(order2.getId(), PaymentMethod.VOUCHER.getValue(), paymentDataVoucher);
        payments.add(payment2);
    }

    @Test
    void testSavePayment() {
        Payment payment = payments.get(1);
        Order order = orders.get(1);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, payment.getMethod(), payment.getPaymentData());
        verify(paymentRepository, times(1)).save(any(Payment.class));

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getMethod(), result.getMethod());
        assertEquals(payment.getPaymentData(), result.getPaymentData());
    }

    @Test
    void testGetPaymentIdFound() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).getPayment(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIdNotFound() {
        doReturn(null).when(paymentRepository).getPayment("zczc");
        assertNull(paymentService.getPayment("zczc"));
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).getAllPayments();
        List <Payment> results = paymentService.getAllPayments();
        assertEquals(payments.size(), results.size());
    }

    @Test
    void testGetAllPaymentsIfEmpty() {
        List<Payment> result = paymentService.getAllPayments();
        verify(paymentRepository, times(1)).getAllPayments();
        assertEquals(0, result.size());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = payments.get(1);
        Order order = orders.get(1);
        order.setStatus(PaymentStatus.SUCCESS.getValue());
        Payment editedStatusPayment = new Payment(payment.getId(), payment.getMethod(), PaymentStatus.SUCCESS.getValue(), payment.getPaymentData());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        doReturn(order).when(orderRepository).save(any(Order.class));
        doReturn(order).when(orderRepository).findById(payment.getId());
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository, times(1)).findById(payment.getId());
        assertEquals(editedStatusPayment.getStatus(), result.getStatus());
        assertEquals(PaymentStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = payments.get(1);
        Order order = orders.get(1);
        order.setStatus("FAILED");
        Payment editedStatusPayment = new Payment(payment.getId(), payment.getMethod(), PaymentStatus.REJECTED.getValue(), payment.getPaymentData());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        doReturn(order).when(orderRepository).save(any(Order.class));
        doReturn(order).when(orderRepository).findById(payment.getId());
        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository, times(1)).findById(payment.getId());
        assertEquals(editedStatusPayment.getStatus(), result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = payments.get(1);
        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW"));
    }

    @Test
    void testSetStatusCheckPayment() {
        // Using the first payment and order from the setup.
        Payment payment = payments.get(0);
        Order order = orders.get(0);

        // Setup mocks for repository calls.
        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        doReturn(order).when(orderRepository).findById(payment.getId());
        doReturn(order).when(orderRepository).save(any(Order.class));

        // Set status to CHECK_PAYMENT which should map to WAITING_PAYMENT.
        Payment result = paymentService.setStatus(payment, PaymentStatus.CHECK_PAYMENT.getValue());

        // Verify interactions.
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).findById(payment.getId());
        verify(orderRepository, times(1)).save(any(Order.class));

        // Assert that payment status remains as CHECK_PAYMENT and order status is updated accordingly.
        assertEquals(PaymentStatus.CHECK_PAYMENT.getValue(), result.getStatus());
        assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatusWithExistingOrder() {
        Payment payment = spy(payments.get(1));
        Order order = orders.get(1);

        // Override setStatus di Payment agar tidak melakukan validasi
        doNothing().when(payment).setStatus(anyString());

        // Stub repository sesuai kebutuhan.
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        // Simpan id payment ke variabel agar tidak memicu pemanggilan langsung saat stubbing.
        String paymentId = payment.getId();
        doReturn(order).when(orderRepository).findById(paymentId);

        // Karena status "MEOW" tidak valid, seharusnya checkStatus melempar IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.setStatus(payment, "MEOW")
        );

        // Verifikasi bahwa findById dipanggil dengan ID payment yang sesuai.
        verify(orderRepository, times(1)).findById(paymentId);
    }
}