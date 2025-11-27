package linh.sunhouse_apartment.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageResponse {
    private Integer id;
    private String name;
    private String image;
    private String status;
    private Date dueDate;
}
