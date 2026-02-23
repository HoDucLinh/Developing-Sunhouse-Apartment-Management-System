package linh.sunhouse_apartment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentInfoResponse {
    private Integer id;
    private String name;
    private String address;
    private String hotline;
    private String email;
    private String description;
    private List<ApartmentImageResponse> images;
}
