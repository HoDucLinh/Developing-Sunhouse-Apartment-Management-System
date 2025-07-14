package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Room;

import java.util.List;

public interface RoomRepository {
    List<Room> findAll();
    Room findById(Integer id);
    List<Room> findByFloorId(Integer floorId);
}
