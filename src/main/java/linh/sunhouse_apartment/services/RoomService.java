package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Room;

import java.util.List;

public interface RoomService {
    List<Room> findAll();
    Room findById(Integer id);
    List<Room> findByFloorId(Integer floorId);
}
