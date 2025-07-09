package linh.sunhouse_apartment.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import linh.sunhouse_apartment.configs.JWTConfig;
import linh.sunhouse_apartment.services.JWTService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    private final JWTConfig jwtConfig;
    private final Key key;

    public JWTServiceImpl(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecret().getBytes()));
    }

    @Override
    public String generateToken(int Id, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());
        return Jwts.builder()
                .setSubject(String.valueOf(Id))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .claim("userName", username)
                .compact();
    }
}
