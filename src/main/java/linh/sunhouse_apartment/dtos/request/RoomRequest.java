package linh.sunhouse_apartment.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private Integer maxPeople;
    private Double area;
    private String description;
    private Integer floorId;
}
