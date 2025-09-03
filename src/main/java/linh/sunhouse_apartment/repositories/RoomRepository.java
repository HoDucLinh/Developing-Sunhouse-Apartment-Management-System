package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.entity.User;

import java.util.List;

public interface RoomRepository {
    List<RoomResponse> findAll(String keyword);
    Room findById(Integer id);
    List<Room> findByFloorId(Integer floorId);
    RoomResponse findRoomWithUsers(Integer roomId);
    void updateRoomHead(Integer roomId, Integer newHeadId);
    List<User> getUsersByRoomId(Room room);
    Room update (Room room);
    Room addRoom(Room room);
    Room findRoomWithRoomNumber(String roomNumber);
}
