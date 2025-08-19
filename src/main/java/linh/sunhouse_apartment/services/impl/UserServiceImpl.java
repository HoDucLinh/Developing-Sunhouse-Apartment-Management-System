package linh.sunhouse_apartment.services.impl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.dtos.request.UpdateProfileRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.dtos.response.UserResponse;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.LockerRepository;
import linh.sunhouse_apartment.repositories.RoomRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.JWTService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
            User user = userRepository.getUserByUserName(request.getUsername());
            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                throw new BadCredentialsException("Username or password invalid");
            }
            String token = jwtService.generateToken(user.getId(), user.getUsername());

            return new AuthenticationResponse(token, user.getUsername());

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Wrong username or password");
        }
    }

    @Override
    public boolean createUser(User user) {
        // Mã hóa mật khẩu
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        user.setCreatedAt(new Date());
        user.setAvatarUrl("https://res.cloudinary.com/dzwsdpjgi/image/upload/v1748436782/avatar_trang_1_cd729c335b_aiu2nl.jpg");
        user.setIsActive(Boolean.TRUE);
        userRepository.saveUser(user);
        if(user.getRole() == User.Role.RESIDENT){
            lockerRepository.addLocker(user);
        }
        return true;
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User editProfile(Integer id, EditProfileRequest dto, MultipartFile file) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Cập nhật các trường chỉ khi có dữ liệu hợp lệ
        if (StringUtils.hasText(dto.getFullName())) {
            user.setFullName(dto.getFullName());
        }

        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }

        if (StringUtils.hasText(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }

        // Upload avatar nếu có file
        if (file != null && !file.isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                user.setAvatarUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Upload avatar failed", e);
            }
        }

        // Đổi mật khẩu nếu có newPassword
        if (StringUtils.hasText(dto.getNewPassword()) && StringUtils.hasText(dto.getConfirmPassword())) {
            if (!passwordEncoder.matches(dto.getConfirmPassword(), user.getPassword())) {
                throw new RuntimeException("Password doesn't match");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        return userRepository.editProfile(user);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return userRepository.getAllUsers(params);
    }

    @Override
    public int blockUser(Integer id) {
        return userRepository.blockUser(id);
    }

    @Override
    public UserResponse getProfileForClient(String username) {
        User user = userRepository.getUserByUserName(username);

        Room room = user.getRoomId();
        List<User> users = roomRepository.getUsersByRoomId(room);
        int available = room.getMaxPeople() - (users != null ? users.size() : 0);


        RoomResponse roomResponse = null;
        if (room != null) {
            roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setRoomNumber(room.getRoomNumber());
            roomResponse.setFloorId(room.getFloor() != null ? room.getFloor().getId() : null);
            roomResponse.setMaxPeople(room.getMaxPeople());
            roomResponse.setArea(room.getArea());
            roomResponse.setDescription(room.getDescription());
            roomResponse.setAvailableSlots(available);
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatarUrl(),
                roomResponse
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username);

        if ("RESIDENT".equals(user.getRole().name())) {
            throw new UsernameNotFoundException("Access denied: not an ADMIN");
        }

        return new CustomUserDetail(user);
    }

    @Override
    public UserDetails loadUserByUsernameForClient(String username) {
        User user = userRepository.getUserByUserName(username);
        return new CustomUserDetail(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return false;
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // Sai mật khẩu cũ
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.editProfile(user);
        return true;
    }
    @Override
    public User updateProfile(int userId, UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return null;
        }
        if (updateProfileRequest.getFullName() != null &&
                !updateProfileRequest.getFullName().equals(user.getFullName())) {
            user.setFullName(updateProfileRequest.getFullName());
        }

        if (updateProfileRequest.getEmail() != null &&
                !updateProfileRequest.getEmail().equals(user.getEmail())) {
            user.setEmail(updateProfileRequest.getEmail());
        }

        if (updateProfileRequest.getPhone() != null &&
                !updateProfileRequest.getPhone().equals(user.getPhone())) {
            user.setPhone(updateProfileRequest.getPhone());
        }
        if (updateProfileRequest.getFile() != null && !updateProfileRequest.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(updateProfileRequest.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                user.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return userRepository.editProfile(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUserName(username);
    }

    @Override
    public List<User> getAllRoomHead() {
        return userRepository.getAllRoomHead();
    }


}
