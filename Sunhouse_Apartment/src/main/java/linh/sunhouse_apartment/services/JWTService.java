package linh.sunhouse_apartment.services;


import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.configs.JWTConfig;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(int Id, String username);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
}
