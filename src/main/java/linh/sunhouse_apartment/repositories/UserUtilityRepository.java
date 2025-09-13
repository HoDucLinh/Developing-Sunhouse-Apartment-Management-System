package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.UserUtility;

import java.util.List;

public interface UserUtilityRepository {
    List<UserUtility> getUserUtilityOfUser(Integer userId);
    UserUtility addUserUtility(UserUtility userUtility);
    UserUtility getUserUtilityById(Integer Id);
}
