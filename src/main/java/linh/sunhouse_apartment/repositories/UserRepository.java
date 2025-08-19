package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    User getUserByUserName(String username);
    User getUserById(Integer id);
    User saveUser(User user);
    List<User> getAllUsers(Map<String, String> params);
    User editProfile(User user);
    int blockUser(int id);
    List<User> getAllRoomHead();
}
