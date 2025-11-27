package linh.sunhouse_apartment.services.impl;

import jakarta.persistence.EntityNotFoundException;
import linh.sunhouse_apartment.entity.Floor;
import linh.sunhouse_apartment.repositories.FloorRepository;
import linh.sunhouse_apartment.services.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private FloorRepository floorRepository;

    @Override
    public List<Floor> findAll() {
        return floorRepository.findAll();
    }

    @Override
    public Floor findFloorByFloorId(Integer floorId) {
        Floor floor = floorRepository.findFloorById(floorId);
        if(floor == null){
            throw new EntityNotFoundException("Floor Not Found");
        }
        return floor;
    }
}
