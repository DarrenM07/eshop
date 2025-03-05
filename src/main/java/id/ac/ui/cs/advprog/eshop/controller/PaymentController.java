package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // GET /payment/detail
    // Shows a form to input a payment ID for detail lookup.
    @GetMapping("/detail")
    public String showPaymentDetailForm(Model model) {
        // You can pass an empty Payment or simply show a form.
        model.addAttribute("payment", new Payment("", "", null));
        return "PaymentDetail"; // Thymeleaf template for payment detail form.
    }

    // GET /payment/detail/{paymentId}
    // Displays the details of a payment based on its ID.
    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentDetailResult"; // Template to display payment details.
    }

    // GET /payment/admin/list
    // Shows all payments (for admin purposes).
    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "PaymentAdminList"; // Template to list all payments.
    }

    // GET /payment/admin/detail/{paymentId}
    // Displays payment details along with options for admin to reject/accept payment.
    @GetMapping("/admin/detail/{paymentId}")
    public String showPaymentAdminDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentAdminDetail"; // Template with payment details and admin options.
    }

    // POST /payment/admin/set-status/{paymentId}
    // Sets the status of a payment based on its ID and the provided status option.
    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId,
                                   @RequestParam("status") String status,
                                   Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            // Redirect to the admin list if the payment is not found.
            return "redirect:/payment/admin/list";
        }
        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute("payment", updatedPayment);
        return "PaymentStatusResult"; // Template showing updated payment status.
    }
}
