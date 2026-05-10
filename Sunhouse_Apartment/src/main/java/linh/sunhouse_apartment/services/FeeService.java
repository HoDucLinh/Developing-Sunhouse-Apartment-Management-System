package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.response.FeeResponse;
import linh.sunhouse_apartment.dtos.response.UtilityResponse;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FeeService {
    void addFee(Fee fee, MultipartFile file, User user);
    List<Fee> getFees(Map<String, String> params);
    void updateFee(Fee fee, User user);
    Integer deleteFee(Integer id, User user);
    Fee getFeeById(int id);
    List<Fee> getUtilities(Map<String,String> params);
    List<Fee> getFeeOfFee();
    List<UtilityResponse> getUtilitiesOfUser(Integer userId);
}
