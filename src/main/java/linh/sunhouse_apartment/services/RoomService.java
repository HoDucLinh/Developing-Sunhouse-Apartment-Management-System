package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.entity.Room;

import java.util.List;

public interface RoomService {
    List<RoomResponse> findAll(String keyword);
    Room findById(Integer id);
    List<Room> findByFloorId(Integer floorId);
    RoomResponse getRoomWithUsers(Integer roomId);
    void changeRoomHead(Integer roomId, Integer newHeadId);
}
