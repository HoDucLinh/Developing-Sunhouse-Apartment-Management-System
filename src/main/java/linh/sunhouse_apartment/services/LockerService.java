package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.User;

import java.util.List;

public interface LockerService {
    Locker createLocker(User user);
    Locker getLockerById(int userId);
    List<Locker> getAllLockers();
}
