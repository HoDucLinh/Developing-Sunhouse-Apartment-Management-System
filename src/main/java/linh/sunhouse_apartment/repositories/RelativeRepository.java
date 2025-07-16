package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Relative;

import java.util.List;

public interface RelativeRepository {
    List<Relative> getRelativesByUserId(int userId);
    Relative getRelativeById(int id);
}
