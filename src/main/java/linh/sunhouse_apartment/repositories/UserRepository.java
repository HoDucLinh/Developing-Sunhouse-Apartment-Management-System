package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByUserName(String username);
}
