package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
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
    // Displays the form to create a new Order.
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        // To avoid IllegalArgumentException in Order constructor (empty product list not allowed),
        // we supply a default non-empty list with a dummy Product.
        List<Product> defaultProducts = new ArrayList<>();
        defaultProducts.add(new Product()); // Assumes Product has a default constructor.
        Order order = Order.builder()
                .id("")
                .products(defaultProducts)
                .orderTime(System.currentTimeMillis())
                .author("")
                .build();
        model.addAttribute("order", order);
        return "CreateOrder"; // Name of your Thymeleaf template for creating orders.
    }

    // GET /order/history
    // Displays a form where the user inputs their name to view order history.
    @GetMapping("/history")
    public String showOrderHistoryForm(Model model) {
        return "OrderHistory"; // Thymeleaf template for inputting author name.
    }

    // POST /order/history
    // Processes the form and shows all orders made by the specified author.
    @PostMapping("/history")
    public String processOrderHistory(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderHistoryResult"; // Template that displays orders for the author.
    }

    // GET /order/pay/{orderId}
    // Displays the payment order page for the given order.
    @GetMapping("/pay/{orderId}")
    public String showOrderPayPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            // Optionally handle order not found, e.g., redirect to history page.
            return "redirect:/order/history";
        }
        model.addAttribute("order", order);
        return "OrderPay"; // Template for paying an order.
    }

    // POST /order/pay/{orderId}
    // Processes the payment request and returns a page showing a generated payment ID.
    @PostMapping("/pay/{orderId}")
    public String processOrderPayment(@PathVariable String orderId, Model model) {
        // Simulate payment processing by generating a dummy payment ID.
        String paymentId = "PAY-" + orderId;
        model.addAttribute("paymentId", paymentId);
        return "PaymentResult"; // Template displaying the payment ID.
    }
}
