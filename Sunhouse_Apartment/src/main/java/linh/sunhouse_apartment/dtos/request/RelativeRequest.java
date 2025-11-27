package linh.sunhouse_apartment.dtos.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeRequest {
    private String fullName;
    private String phone;
    private String relationship;
    private Integer userId;
}
