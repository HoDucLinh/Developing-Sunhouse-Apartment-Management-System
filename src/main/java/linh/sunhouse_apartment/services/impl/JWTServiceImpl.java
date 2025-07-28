package linh.sunhouse_apartment.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import linh.sunhouse_apartment.configs.JWTConfig;
import linh.sunhouse_apartment.services.JWTService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // ✅ Sử dụng SecretKey
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    private final JWTConfig jwtConfig;
    private final SecretKey key; // ✅ sửa thành SecretKey
    private final MacAlgorithm algorithm = Jwts.SIG.HS512;
    private final JwtParser jwtParser;

    public JWTServiceImpl(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;

        // ✅ Tạo key từ secret
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());

        // ✅ verifyWith yêu cầu SecretKey
        this.jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    @Override
    public String generateToken(int id, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("userName", username)
                .signWith(key, algorithm)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .get("userName", String.class);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}
