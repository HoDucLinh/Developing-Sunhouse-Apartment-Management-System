package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Floor;

import java.util.List;

public interface FloorRepository {
    List<Floor> findAll();
    Floor findFloorById(Integer id);
}
