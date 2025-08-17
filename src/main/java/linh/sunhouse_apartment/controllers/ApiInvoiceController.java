package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/invoice")
@CrossOrigin(origins = "*")
public class ApiInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/addInvoice")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            if (invoiceRequest == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("InvoiceRequest không được để trống");
            }

            InvoiceResponse savedInvoice = invoiceService.saveInvoice(invoiceRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedInvoice);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo invoice: " + e.getMessage());
        }
    }
    @GetMapping("/get-invoices/{userId}")
    public ResponseEntity<?> getInvoicesByUserId(@PathVariable Integer userId) {
        try {
            List<InvoiceResponse> invoices = invoiceService.getInvoicesByUserId(userId);

            if (invoices == null || invoices.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy hóa đơn nào cho userId = " + userId);
            }

            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi lấy danh sách hóa đơn: " + e.getMessage());
        }
    }
    @PutMapping("/cancel-invoice/{invoiceId}")
    public ResponseEntity<?> cancelInvoice(@PathVariable Integer invoiceId) {
        try {
            invoiceService.cancelInvoice(invoiceId);
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // log ra console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi hủy đăng kí");
        }
    }
}
