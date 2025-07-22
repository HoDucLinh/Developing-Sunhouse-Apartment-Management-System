package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.LockerRepository;
import linh.sunhouse_apartment.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LockerServiceImpl implements LockerService {

    @Autowired
    LockerRepository lockerRepository;


    @Override
    public Locker createLocker(User user) {
        return lockerRepository.addLocker(user);
    }

    @Override
    public Locker getLockerById(int userId) {
        return lockerRepository.getLockerByID(userId);
    }

    @Override
    public List<Locker> getAllLockers(String keyword) {
        return lockerRepository.getAllLockers(keyword);
    }
}
