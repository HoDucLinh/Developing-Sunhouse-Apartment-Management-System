package linh.sunhouse_apartment.controllers;

import jakarta.servlet.http.HttpServletRequest;
import linh.sunhouse_apartment.dtos.request.PaymentRequest;
import linh.sunhouse_apartment.services.InvoiceService;
import linh.sunhouse_apartment.services.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/payment/vnpay")
public class VNPayController {
    @Autowired
    private VNPayService vnpayService;

    @PostMapping("/vnpay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody PaymentRequest paymentRequest) {
        String returnUrl = "http://localhost:8081";
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("invoiceId", paymentRequest.getInvoiceId());
        paymentMap.put("amount", paymentRequest.getAmount());

        String response = vnpayService.createOrder(request, paymentMap, "Thanh toan don hang", returnUrl);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/vnpay-payment-return")
    public String returnPayment(HttpServletRequest request) {
        int result = vnpayService.orderReturn(request);
        String orderId = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");

        System.out.println("result: " + result);
        System.out.println("orderId: " + orderId);

        request.getParameterMap().forEach((k, v) -> System.out.println(k + ": " + Arrays.toString(v)));

        if ("00".equals(responseCode)) {
            try {
                return "redirect:http://localhost:3000/payment/result?status=success";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:http://localhost:3000/payment/result?error=invalid_invoice_id";
            }
        } else if ("24".equals(responseCode)) {
            return "redirect:http://localhost:3000/payment/result?status=cancel";
        } else {
            return "redirect:http://localhost:3000/payment/result?status=fail";
        }
    }
}
