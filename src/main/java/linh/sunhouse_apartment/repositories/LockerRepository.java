package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.User;

import java.util.List;


public interface LockerRepository {
    Locker addLocker(User u);
    Locker getLockerByID(int userId); // userId cũng là lockerId
    List<Locker> getAllLockers(String keyword);
}
