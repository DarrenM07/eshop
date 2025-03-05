package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
import enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // Save the order first if it’s not already saved.
        if (orderRepository.findById(order.getId()) == null) {
            orderRepository.save(order);
        }
        Payment payment = new Payment(order.getId(), method, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);
        Order order = orderRepository.findById(payment.getId());
        if (order != null) {
            String newOrderStatus = checkStatus(status);
            order.setStatus(newOrderStatus);
            orderRepository.save(order);
        }
        return payment;
    }

    private String checkStatus(String paymentStatus){
        if (paymentStatus.equals(PaymentStatus.SUCCESS.getValue())){
            return OrderStatus.SUCCESS.getValue();
        } else if (paymentStatus.equals(PaymentStatus.REJECTED.getValue())) {
            return OrderStatus.FAILED.getValue();
        }
        else if (paymentStatus.equals(PaymentStatus.CHECK_PAYMENT.getValue())) {
            return OrderStatus.WAITING_PAYMENT.getValue();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.getPayment(paymentId);
    }
    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

}