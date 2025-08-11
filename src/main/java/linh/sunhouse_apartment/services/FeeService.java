package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Fee;

import java.util.List;
import java.util.Map;

public interface FeeService {
    void addFee(Fee fee);
    List<Fee> getFees(Map<String, String> params);
    void updateFee(Fee fee);
    int deleteFee(int id);
    Fee getFeeById(int id);
}
