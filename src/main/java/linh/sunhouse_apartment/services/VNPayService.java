package linh.sunhouse_apartment.services;
import linh.sunhouse_apartment.configs.VNPConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {
    @Autowired
    private Environment env;

    public String createOrder(HttpServletRequest request,
                              Map<String, Object> orderRequest,
                              String orderInfo,
                              String baseReturnUrl) {

        long invoiceId = Long.parseLong(orderRequest.get("invoiceId").toString());
        double amount = Double.parseDouble(orderRequest.get("amount").toString());

        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ");
        }

        // Thông tin cơ bản
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = String.valueOf(invoiceId);
        String vnp_IpAddr = VNPConfig.getIpAddress(request);
        String vnp_TmnCode = env.getProperty("vnp.tmnCode");

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);

        // Nhân 100 theo yêu cầu của VNPay (VD: 10000 VND → 1000000)
        vnp_Params.put("vnp_Amount", String.valueOf((long) (amount * 100)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);

        // Loại giao dịch hợp lệ
        vnp_Params.put("vnp_OrderType", "other");

        // Locale & ReturnUrl
        vnp_Params.put("vnp_Locale", "vn");
        String returnUrl = env.getProperty("vnp.returnUrl");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thời gian tạo + hết hạn
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // hết hạn sau 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Build query string
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Hash với secret key
        String secretKey = env.getProperty("vnp.hashSecret");
        String vnp_SecureHash = VNPConfig.hmacSHA512(secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        System.out.println("vnp_Params = " + vnp_Params);
        System.out.println("Hash data = " + hashData.toString());
        System.out.println("SecureHash = " + vnp_SecureHash);

        // Trả về link redirect
        return env.getProperty("vnp.payUrl") + "?" + query.toString();
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        // Lấy tham số từ request, KHÔNG encode lại
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Lấy giá trị secure hash từ request
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Loại bỏ tham số không cần thiết để tính hash
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Sắp xếp tham số theo key alphabet
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi dữ liệu để hash
        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            String value = fields.get(key);
            if (value != null && value.length() > 0) {
                hashData.append(key).append('=').append(value);
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }

        // Tính hash với secret key
        String secretKey = env.getProperty("vnp.hashSecret"); // hoặc lấy từ config
        String calculatedHash = VNPConfig.hmacSHA512(secretKey, hashData.toString());

        // So sánh hash (chú ý chữ hoa/thường)
        if (calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            // Kiểm tra trạng thái giao dịch
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1; // thành công
            } else {
                return 0; // thất bại
            }
        } else {
            return -1;
        }
    }
}
