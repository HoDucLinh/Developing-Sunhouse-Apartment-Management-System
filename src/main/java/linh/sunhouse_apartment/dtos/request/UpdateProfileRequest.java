package linh.sunhouse_apartment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    String fullName;
    String email;
    String phone;
    MultipartFile file;
}
