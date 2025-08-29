package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.response.FeeResponse;
import linh.sunhouse_apartment.entity.Fee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FeeService {
    void addFee(Fee fee, MultipartFile file);
    List<Fee> getFees(Map<String, String> params);
    void updateFee(Fee fee);
    int deleteFee(int id);
    Fee getFeeById(int id);
    List<Fee> getUtilities(Map<String,String> params);
    List<Fee> getFeeOfFee();
    List<FeeResponse> getUtilitiesOfUser(Integer userId);
}
