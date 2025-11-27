package linh.sunhouse_apartment.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import linh.sunhouse_apartment.dtos.request.PaymentRequest;
import linh.sunhouse_apartment.services.InvoiceService;
import linh.sunhouse_apartment.services.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/payment")
public class VNPayController {
    @Autowired
    private VNPayService vnpayService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/vnpay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody Map<String, Object> paymentRequest) {
        String returnUrl = "http://localhost:8081/api/payment/vnpay-payment-return";
        String response = vnpayService.createOrder(request, paymentRequest, "Thanh toan don hang", returnUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment-return")
    public void returnPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int result = vnpayService.orderReturn(request);
        String orderId = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String bankCode = request.getParameter("vnp_BankCode");
        String amount = request.getParameter("vnp_Amount");
        String payDate = request.getParameter("vnp_PayDate");

        String finalCode = (responseCode != null) ? responseCode : transactionStatus;

        String redirectUrl = "http://localhost:3000/payment/result"
                + "?invoiceId=" + orderId
                + "&vnp_ResponseCode=" + finalCode;

        if ("00".equals(transactionStatus)) {
            redirectUrl += "&vnp_TransactionNo=" + transactionNo
                    + "&vnp_BankCode=" + bankCode
                    + "&vnp_Amount=" + amount
                    + "&vnp_PayDate=" + payDate;
        }

        response.sendRedirect(redirectUrl);
    }


}
