package linh.sunhouse_apartment.dtos.request;

import linh.sunhouse_apartment.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {
    private String name;
    private MultipartFile file;
    private Integer lockerId;
}
