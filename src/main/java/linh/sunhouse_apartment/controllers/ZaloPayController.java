package linh.sunhouse_apartment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import linh.sunhouse_apartment.dtos.request.PaymentRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class ZaloPayController {
    private final Integer APP_ID = 2553 ;
    private final String KEY1 = "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL";
    private final String CREATE_ORDER_URL = "https://sb-openapi.zalopay.vn/v2/create";

    @PostMapping("/zalopay")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest req) throws Exception {
        long appTime = System.currentTimeMillis();
        String appTransId = new SimpleDateFormat("yyMMdd").format(new Date()) + "_" + (int)(Math.random() * 1000000);

        ObjectMapper mapper = new ObjectMapper();

        // item & embed_data phải serialize thành string
        List<Object> item = new ArrayList<>();
        Map<String, Object> embedData = new HashMap<>();
        String itemJson = mapper.writeValueAsString(item);
        String embedDataJson = mapper.writeValueAsString(embedData);

        // Tính MAC
        String data = APP_ID + "|" + appTransId + "|" + req.getUserId() + "|" + req.getAmount()
                + "|" + appTime + "|" + embedDataJson + "|" + itemJson;

        String mac = hmacSHA256(KEY1, data);

        // Payload chuẩn theo doc
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", APP_ID);
        order.put("app_user", req.getUserId());
        order.put("app_trans_id", appTransId);
        order.put("app_time", appTime);
        order.put("amount", req.getAmount());
        order.put("title", "Thanh toán hóa đơn");
        order.put("description", "Thanh toán hóa đơn #" + req.getInvoiceId());
        order.put("callback_url", "http://localhost:8081/payment/callback");
        order.put("redirect_url", "http://localhost:3000/payment/result?invoiceId=" + req.getInvoiceId());
        order.put("item", itemJson);
        order.put("embed_data", embedDataJson);
        order.put("mac", mac);

        // Call API
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(order, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(CREATE_ORDER_URL, entity, String.class);

        Map<String, Object> resMap = mapper.readValue(response.getBody(), Map.class);
        return ResponseEntity.ok(Map.of(
                "order_url", resMap.get("order_url"),
                "app_trans_id", appTransId,
                "invoiceId", req.getInvoiceId()
        ));
    }


    private String hmacSHA256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash); // chuyển byte[] -> hex string
    }
}
