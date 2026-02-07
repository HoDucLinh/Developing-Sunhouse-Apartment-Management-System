package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidRoomResponse {
    private String fullName;
    private Integer roomId;
    private String roomNumber;
    private Integer floorNumber;
    private BigDecimal rentPrice;
}
