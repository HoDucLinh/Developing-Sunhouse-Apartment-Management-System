package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.JWTService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
            User user = userRepository.getUserByUserName(request.getUsername()).orElseThrow(() ->new BadCredentialsException("Username or password invalid"));
            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                throw new BadCredentialsException("Username or password invalid");
            }
            String token = jwtService.generateToken(user.getId(), user.getUsername());

            return new AuthenticationResponse(token, user.getUsername());

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Wrong username or password");
        }
    }
}
