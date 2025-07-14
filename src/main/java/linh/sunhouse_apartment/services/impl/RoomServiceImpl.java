package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.repositories.RoomRepository;
import linh.sunhouse_apartment.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> findByFloorId(Integer floorId) {
        return roomRepository.findByFloorId(floorId);
    }
}
