package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // GET /order/create
    // Displays the create order form.
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        // Since Order constructor does not allow an empty product list,
        // we supply a dummy product to satisfy the requirement.
        List<Product> dummyProducts = new ArrayList<>();
        dummyProducts.add(new Product());
        Order order = Order.builder()
                .id("")
                .products(dummyProducts)
                .orderTime(System.currentTimeMillis())
                .author("")
                .status(OrderStatus.WAITING_PAYMENT.getValue()) // explicitly set valid status
                .build();
        model.addAttribute("order", order);
        return "CreateOrder";
    }

    // GET /order/history
    // Shows the form for the user to input their name to view order history.
    @GetMapping("/history")
    public String showOrderHistoryForm() {
        return "OrderHistory";
    }

    // POST /order/history
    // Processes the form submission and displays orders for the provided author.
    @PostMapping("/history")
    public String processOrderHistory(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderHistoryResult";
    }

    // GET /order/pay/{orderId}
    // Displays the payment page for a given order.
    @GetMapping("/pay/{orderId}")
    public String showOrderPayPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            // If the order is not found, redirect to order history form.
            return "redirect:/order/history";
        }
        model.addAttribute("order", order);
        return "OrderPay";
    }

    // POST /order/pay/{orderId}
    // Processes the payment request and returns a page with a generated payment ID.
    @PostMapping("/pay/{orderId}")
    public String processOrderPayment(@PathVariable String orderId, Model model) {
        // Simulate payment processing by generating a dummy payment ID using the order ID.
        String paymentId = "PAY-" + orderId;
        model.addAttribute("paymentId", paymentId);
        return "PaymentResult";
    }
}
