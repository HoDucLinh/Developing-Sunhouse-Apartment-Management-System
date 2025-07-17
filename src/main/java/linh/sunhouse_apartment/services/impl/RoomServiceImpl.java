package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.response.RoomResponse;
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
    public List<RoomResponse> findAll(String keyword) {
        return roomRepository.findAll(keyword);
    }

    @Override
    public RoomResponse getRoomWithUsers(Integer roomId) {
        return roomRepository.findRoomWithUsers(roomId);
    }

    @Override
    public void changeRoomHead(Integer roomId, Integer newHeadId) {
        roomRepository.updateRoomHead(roomId, newHeadId);
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
