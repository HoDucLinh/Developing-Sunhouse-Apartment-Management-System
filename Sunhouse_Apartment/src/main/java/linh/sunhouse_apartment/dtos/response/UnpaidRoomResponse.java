package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidRoomResponse {
    private String fullName;
    private String email;
    private Integer roomId;
    private String roomNumber;
    private Integer floorNumber;
    private BigDecimal rentPrice;
    private Date dueDate;
    private String feeName;
}
