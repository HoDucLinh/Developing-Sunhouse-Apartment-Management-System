package linh.sunhouse_apartment.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Integer id;
    private String roomNumber;
    private Integer floorId;
    private Integer maxPeople;
    private Double area;
    private String description;
    private Integer availableSlots;
    private List<UserResponse> users;
    private Integer roomHeadId;
    private BigDecimal rent_price;
}
