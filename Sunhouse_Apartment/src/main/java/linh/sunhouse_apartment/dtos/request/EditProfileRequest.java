package linh.sunhouse_apartment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private String fullName;
    private String email;
    private String phone;
    private String newPassword;
    private String confirmPassword;
}
