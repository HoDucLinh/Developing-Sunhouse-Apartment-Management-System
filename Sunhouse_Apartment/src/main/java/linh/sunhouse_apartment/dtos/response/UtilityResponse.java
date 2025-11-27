package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilityResponse {
    private String feeName;
    private Double feeAmount;
    private Date startDate;
    private Date endDate;
}
