package linh.sunhouse_apartment.dtos.response;

import linh.sunhouse_apartment.entity.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private Integer id;
    private Date issuedDate;
    private Date dueDate;
    private Invoice.PAYMENT_METHOD paymentMethod;
    private String paymentProof;
    private BigDecimal totalAmount;
    private Invoice.Status status;
    private Boolean isAccepted;
    private String fullName;

    private List<DetailInvoiceResponse> details;
}
