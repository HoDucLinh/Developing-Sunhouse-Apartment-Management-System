package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.dtos.request.UpdateProfileRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.dtos.response.UserResponse;
import linh.sunhouse_apartment.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService  {
    AuthenticationResponse authenticate (AuthenticationRequest request);
    boolean createUser(User user);
    User getUserById(Integer id);
    User editProfile(Integer id, EditProfileRequest dto, MultipartFile file);
    List<User> getUsers(Map<String, String> params);
    int blockUser(Integer id);
    UserResponse getProfileForClient(String username);
    UserDetails loadUserByUsernameForClient(String username);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    User updateProfile(int userId, UpdateProfileRequest updateProfileRequest);
    User getUserByUsername(String username);
    List<User> getAllRoomHead();
    Map<Integer, Long> getResidentStatistics(int year, String period);
    User changeUserRole(Integer userId, User.Role role);
    User forgotPassword(String username, String email);
}
