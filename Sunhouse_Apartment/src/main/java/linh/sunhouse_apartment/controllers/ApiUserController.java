package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.dtos.request.UpdateProfileRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.dtos.response.UserResponse;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = userService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/secure/profile")
    public ResponseEntity<UserResponse> getCurrentUserInfo(Principal principal) {
        return ResponseEntity.ok(userService.getProfileForClient(principal.getName()));
    }
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable("id") int userId,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {

        try {
            boolean success = userService.changePassword(userId, oldPassword, newPassword);

            if (success) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Đổi mật khẩu thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Mật khẩu cũ không đúng hoặc người dùng không tồn tại"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }
    @PutMapping(path = "/update-profile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @PathVariable("id") int userId,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            UpdateProfileRequest request = new UpdateProfileRequest();
            request.setFullName(fullName);
            request.setEmail(email);
            request.setPhone(phone);
            request.setFile(file);

            User updatedUser = userService.updateProfile(userId, request);

            if (updatedUser != null) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật hồ sơ thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Người dùng không tồn tại"));
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }
}
