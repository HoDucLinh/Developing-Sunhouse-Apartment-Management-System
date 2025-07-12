package linh.sunhouse_apartment.services.impl;
import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.JWTService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

//    @Override
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        try{
//            User user = userRepository.getUserByUserName(request.getUsername()).orElseThrow(() ->new BadCredentialsException("Username or password invalid"));
//            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
//                throw new BadCredentialsException("Username or password invalid");
//            }
//            String token = jwtService.generateToken(user.getId(), user.getUsername());
//
//            return new AuthenticationResponse(token, user.getUsername());
//
//        } catch (BadCredentialsException e) {
//            throw new RuntimeException("Wrong username or password");
//        }
//    }

    @Override
    public boolean createUser(User user) {
        // Mã hóa mật khẩu
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        user.setRole("ADMIN");
        user.setCreatedAt(new Date());
        user.setIsActive(Boolean.TRUE);
        user.setAvatarUrl("https://res.cloudinary.com/dzwsdpjgi/image/upload/v1748436782/avatar_trang_1_cd729c335b_aiu2nl.jpg");
        user.setRoomId(null);
        userRepository.saveUser(user);
        return true;
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User editProfile(Integer id, EditProfileRequest dto, MultipartFile file) {
        User user = userRepository.getUserById(id);
        if (user == null)
            throw new RuntimeException("User not found");

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                user.setAvatarUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Upload avatar failed", e);
            }
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Mật khẩu xác nhận không khớp");
            }
            user.setPassword(new BCryptPasswordEncoder().encode(dto.getNewPassword()));
        }

        return userRepository.editProfile(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new UsernameNotFoundException("Access denied: not an ADMIN");
        }

        return new CustomUserDetail(user);
    }

}
