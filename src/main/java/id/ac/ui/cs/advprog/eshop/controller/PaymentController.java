package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // GET /payment/detail
    // Shows the payment detail form.
    @GetMapping("/detail")
    public String showPaymentDetailForm(Model model) {
        // Create a dummy Payment object for form binding.
        // Since Payment requires an id, method, and paymentData,
        // we provide empty values.
        model.addAttribute("payment", new Payment("", "COD", new HashMap<>()));
        return "PaymentDetail";  // Thymeleaf template for the detail form.
    }

    // GET /payment/detail/{paymentId}
    // Shows the details of a payment based on its ID.
    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentDetailResult";  // Template to display payment details.
    }

    // GET /payment/admin/list
    // Shows all payments (for admin).
    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "PaymentAdminList";  // Template for admin payment list.
    }

    // GET /payment/admin/detail/{paymentId}
    // Shows details of a payment for admin with options to reject/accept.
    @GetMapping("/admin/detail/{paymentId}")
    public String showPaymentAdminDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentAdminDetail";  // Template with payment details and admin options.
    }

    // POST /payment/admin/set-status/{paymentId}
    // Sets the status of a payment based on its ID and provided status option.
    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId,
                                   @RequestParam("status") String status,
                                   Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return "redirect:/payment/admin/list";
        }
        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute("payment", updatedPayment);
        return "PaymentStatusResult";  // Template showing updated payment status.
    }
}
