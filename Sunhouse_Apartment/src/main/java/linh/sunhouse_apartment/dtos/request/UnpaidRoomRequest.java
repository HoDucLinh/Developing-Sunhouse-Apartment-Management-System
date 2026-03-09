package linh.sunhouse_apartment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidRoomRequest {
    private String email;
    private String name;
    private String fee;
    private BigDecimal amount;
    private Date date;
}
