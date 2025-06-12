package com.cineflix.controller;

import com.cineflix.entity.PaymentRequest;
import com.cineflix.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

   @PostMapping("/pay")
    @ResponseBody // ✅ esto asegura que retorne JSON incluso en @Controller
    public Map<String, String> createPayment(@RequestBody PaymentRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = paypalService.createPayment(request);
            response.put("status", "success");
            response.put("redirect_url", payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .get()
                    .getHref());
        } catch (PayPalRESTException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @GetMapping("/execute")
    public String executePayment(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            HttpSession session) throws PayPalRESTException {

        paypalService.executePayment(paymentId, payerId);

        // ✅ Redirige a una vista tipo success.html (Thymeleaf)
        return "redirect:/success";
    }

}
