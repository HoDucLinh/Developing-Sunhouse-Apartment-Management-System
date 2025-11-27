package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.RelativeRequest;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.RelativeRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RelativeServiceImpl implements RelativeService {

    @Autowired
    private RelativeRepository relativeRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<Relative> getRelativesByUserId(int userId) {
        return relativeRepo.getRelativesByUserId(userId);
    }

    @Override
    public Relative getRelativeById(int id) {
        return relativeRepo.getRelativeById(id);
    }

    @Override
    public Relative addRelative(RelativeRequest relativeRequest) {
        User user = userRepo.getUserById(relativeRequest.getUserId());
        if(user != null) {
            Relative relative = new Relative();
            relative.setCreatedAt(new Date());
            try {
                relative.setRelationship(Relative.EnumRelationship.valueOf(relativeRequest.getRelationship().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Relationship không hợp lệ. Chỉ chấp nhận: OWNER, PARENT.");
            }
            relative.setFullName(relativeRequest.getFullName());
            relative.setUserId(user);
            relative.setPhone(relativeRequest.getPhone());
            return relativeRepo.addRelative(relative);
        }
        if (user == null) {
            throw new RuntimeException("Không tìm thấy user với id = " + relativeRequest.getUserId());
        }
        return null;

    }
}
