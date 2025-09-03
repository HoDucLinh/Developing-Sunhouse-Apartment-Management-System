package linh.sunhouse_apartment.services.impl;

import jakarta.persistence.EntityNotFoundException;
import linh.sunhouse_apartment.dtos.request.RoomRequest;
import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.entity.Floor;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.repositories.FloorRepository;
import linh.sunhouse_apartment.repositories.RoomRepository;
import linh.sunhouse_apartment.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FloorRepository floorRepository;

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
    public Room updateRoom(Integer roomId, RoomRequest dto) {
        Room existingRoom = roomRepository.findById(roomId);
        if (existingRoom == null) {
            throw new EntityNotFoundException("Room not found with id " + roomId);
        }

        if (dto.getRoomNumber() != null) {
            existingRoom.setRoomNumber(dto.getRoomNumber());
        }
        if (dto.getMaxPeople() != null) {
            existingRoom.setMaxPeople(dto.getMaxPeople());
        }
        if (dto.getArea() != null) {
            existingRoom.setArea(dto.getArea());
        }
        if (dto.getDescription() != null) {
            existingRoom.setDescription(dto.getDescription());
        }
        if (dto.getFloorId() != null) {
            Floor floor = new Floor();
            floor.setId(dto.getFloorId());
            existingRoom.setFloor(floor);
        }

        return roomRepository.update(existingRoom);
    }

    @Override
    public Room addRoom(RoomRequest dto) {
        Room room = roomRepository.findRoomWithRoomNumber(dto.getRoomNumber());
        Floor floor = floorRepository.findFloorById(dto.getFloorId());
        if(room != null){
            throw new EntityNotFoundException("Room already exists with room number " + dto.getRoomNumber());
        }
        if(floor == null){
            throw new EntityNotFoundException("Floor not found with floor id " + dto.getFloorId());
        }
        room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setMaxPeople(dto.getMaxPeople());
        room.setArea(dto.getArea());
        room.setDescription(dto.getDescription());
        room.setFloor(floor);
        roomRepository.addRoom(room);
        return room;
    }


    @Override
    public Room findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> findByFloorId(Integer floorId) {
        List<Room> rs = new ArrayList<>();
        List<Room> rooms = roomRepository.findByFloorId(floorId);
        for (Room room : rooms) {
            RoomResponse r = roomRepository.findRoomWithUsers(room.getId());
            if (r.getUsers().size() < room.getMaxPeople())
                rs.add(room);
        }
        return rs;
    }
}
