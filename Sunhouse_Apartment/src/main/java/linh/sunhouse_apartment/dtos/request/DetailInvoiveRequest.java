package linh.sunhouse_apartment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailInvoiveRequest {
    private Integer feeId;      // ID của Fee
    private BigDecimal amount;  // Số tiền
    private String note;
}
