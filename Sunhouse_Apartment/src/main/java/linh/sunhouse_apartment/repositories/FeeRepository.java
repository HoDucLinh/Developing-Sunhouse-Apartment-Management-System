package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.User;

import java.util.List;
import java.util.Map;

public interface FeeRepository {
    void addFee(Fee fee);
    List<Fee> getFees(Map<String, String> params);
    void updateFee(Fee fee, User user);
    int deleteFee(int id);
    Fee getFeeById(int id);
    List<Fee> getUtilities(Map<String,String> params);
    List<Fee> getFeeOfFee();
}
