package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.RelativeRequest;
import linh.sunhouse_apartment.entity.Relative;

import java.util.List;

public interface RelativeService {
    List<Relative> getRelativesByUserId(int userId);
    Relative getRelativeById(int id);
    Relative addRelative(RelativeRequest relativeRequest);
}
