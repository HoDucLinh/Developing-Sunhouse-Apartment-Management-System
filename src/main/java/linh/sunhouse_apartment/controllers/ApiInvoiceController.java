package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
