package linh.sunhouse_apartment.dtos.request;


import linh.sunhouse_apartment.entity.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private Integer userId;
    private Invoice.PAYMENT_METHOD paymentMethod;
    private String paymentProof;
    private BigDecimal totalAmount;
    private List<DetailInvoiveRequest> details;
}
