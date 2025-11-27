package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailInvoiceResponse {
    private Integer feeId;
    private String feeName;
    private BigDecimal amount;
    private String note;
}
