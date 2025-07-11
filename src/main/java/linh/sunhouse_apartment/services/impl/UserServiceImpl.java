package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.getUserByUserName(username).orElseThrow(() -> new BadCredentialsException("User not found"));
        String password = null;
        List<GrantedAuthority> grantedAuthorities;
        username = u.getUsername();
        password = u.getPassword();
        if (!"ADMIN".equals(u.getRole())) {
            throw new UsernameNotFoundException("Access denied: not an ADMIN");
        }
        grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(u.getRole()));
        return new org.springframework.security.core.userdetails.User(u.getUsername(), password, grantedAuthorities);
    }

}
