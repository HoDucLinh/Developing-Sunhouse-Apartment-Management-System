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

    public String getReturnUrl() {
        return env.getProperty("vnp.returnUrl"); // callback URL
    }

    /**
     * Tạo URL thanh toán VNPAY theo đúng spec (urlencode cả key & value, sắp xếp alphabet).
     * Truyền vào urlReturn từ Controller (đảm bảo đúng URL).
     */
    public String createOrder(HttpServletRequest request, Map<String, Object> orderRequest,
                              String orderInfor, String urlReturn) {
        // --- Lấy dữ liệu cơ bản ---
        Integer invoiceId = Integer.parseInt(orderRequest.get("invoiceId").toString());
        // VNPay yêu cầu amount nhân 100 (loại bỏ phần thập phân)
        long amount = (long) (Double.parseDouble(orderRequest.get("amount").toString()) * 100);

        String vnp_TxnRef = String.valueOf(invoiceId);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_IpAddr = VNPConfig.getIpAddress(request);
        String vnp_TmnCode = env.getProperty("vnp.tmnCode");

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", urlReturn); // dùng đúng returnUrl controller truyền vào
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Nếu FE gửi bankCode (tùy chọn) thì thêm
        Object bankCode = orderRequest.get("bankCode");
        if (bankCode != null && !bankCode.toString().isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode.toString());
        } else {
            vnp_Params.put("vnp_BankCode", "NCB"); // test bank
        }

        // createDate + expireDate (GMT+7)
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // --- Sắp xếp tham số theo alphabet ---
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // --- Build hashData (raw) and query (encoded) ---
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                // SỬA ĐỔI CUỐI CÙNG: Mã hóa giá trị trước khi thêm vào hashData
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }


                //Build query
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // --- Sinh secure hash (HMAC-SHA512) trên chuỗi hashData (UTF-8) ---
        String secretKey = env.getProperty("vnp.hashSecret");
        if (secretKey != null) secretKey = secretKey.trim();

        String vnp_SecureHash = VNPConfig.hmacSHA512(secretKey, hashData.toString());

        // Thêm vào query (không đưa vào hashData)
        query.append("&vnp_SecureHashType=HMACSHA512");
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);


        String payUrl = env.getProperty("vnp.payUrl") + "?" + query.toString();

        // --- Debug (in ra để đối chiếu khi bị lỗi chữ ký) ---
        System.out.println("=== VNPay CREATE ORDER DEBUG ===");
        System.out.println("hashData = " + hashData.toString());
        System.out.println("vnp_SecureHash = " + vnp_SecureHash);
        System.out.println("payUrl = " + payUrl);
        System.out.println("================================");

        return payUrl;
    }

    /**
     * Xử lý callback/return từ VNPay: kiểm tra secure hash và trả về trạng thái
     * 1 = success, 0 = fail, -1 = invalid hash
     */
    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        // Loại bỏ 2 tham số không dùng khi tính lại hash
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // --- Build hashData (raw) ---
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                // SỬA ĐỔI CUỐI CÙNG: Mã hóa giá trị trước khi thêm vào hashData
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String secretKey = env.getProperty("vnp.hashSecret");
        if (secretKey != null) secretKey = secretKey.trim();
        String calculatedHash = VNPConfig.hmacSHA512(secretKey, hashData.toString());

        // Debug callback
        System.out.println("=== VNPay CALLBACK DEBUG ===");
        System.out.println("received vnp_SecureHash = " + vnp_SecureHash);
        System.out.println("calculatedHash          = " + calculatedHash);
        System.out.println("hashData                = " + hashData.toString());
        System.out.println("============================");

        if (!calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            System.err.println("VNPay hash mismatch!");
            return -1;
        }

        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        return "00".equals(transactionStatus) ? 1 : 0;
    }
}