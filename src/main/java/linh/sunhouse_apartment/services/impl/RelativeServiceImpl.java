package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.repositories.RelativeRepository;
import linh.sunhouse_apartment.services.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelativeServiceImpl implements RelativeService {

    @Autowired
    private RelativeRepository relativeRepo;

    @Override
    public List<Relative> getRelativesByUserId(int userId) {
        return relativeRepo.getRelativesByUserId(userId);
    }

    @Override
    public Relative getRelativeById(int id) {
        return relativeRepo.getRelativeById(id);
    }
}
