package linh.sunhouse_apartment.services;


import linh.sunhouse_apartment.configs.JWTConfig;

public interface JWTService {
    String generateToken(int Id, String username);
}
