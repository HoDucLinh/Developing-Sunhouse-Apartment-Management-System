package linh.sunhouse_apartment.dtos.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeRequest {
    private String fullName;
    private String phone;
    private String relationship;
    private Integer userId;
    private Date createdAt;
    private Date expiredAt;
}
