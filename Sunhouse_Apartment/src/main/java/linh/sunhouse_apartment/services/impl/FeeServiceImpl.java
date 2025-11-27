package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.dtos.response.FeeResponse;
import linh.sunhouse_apartment.dtos.response.UtilityResponse;
import linh.sunhouse_apartment.entity.*;
import linh.sunhouse_apartment.repositories.*;
import linh.sunhouse_apartment.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserUtilityRepository userUtilityRepository;

    @Override
    public void addFee(Fee fee, MultipartFile file) {
        if (fee == null) {
            throw new IllegalArgumentException("Fee object must not be null");
        }
        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                fee.setImage(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Upload image failed", e);
            }
        }
        feeRepository.addFee(fee);
    }

    @Override
    public List<Fee> getFees(Map<String, String> params) {
        return feeRepository.getFees(params);
    }
    @Override
    public void updateFee(Fee fee) {
        if (fee == null) throw new IllegalArgumentException("Fee object must not be null");
        feeRepository.updateFee(fee);
    }

    @Override
    public int deleteFee(int id) {
        return feeRepository.deleteFee(id);
    }

    @Override
    public Fee getFeeById(int id) {
        return feeRepository.getFeeById(id);
    }

    @Override
    public List<Fee> getUtilities(Map<String,String> params) {
        return feeRepository.getUtilities(params);
    }

    @Override
    public List<Fee> getFeeOfFee() {
        return feeRepository.getFeeOfFee();
    }

    @Override
    public List<UtilityResponse> getUtilitiesOfUser(Integer userId) {
        User u = userRepository.getUserById(userId);
        if (u == null)
            throw new RuntimeException("Not found user " + userId);
        List<UtilityResponse> utilities = new ArrayList<>();
        List<UserUtility> userUtilities = userUtilityRepository.getUserUtilityOfUser(u.getId());
        for (UserUtility userUtility : userUtilities) {
            UtilityResponse utilityResponse = new UtilityResponse();
            utilityResponse.setFeeName(userUtility.getFee().getName());
            utilityResponse.setFeeAmount(userUtility.getFee().getPrice());
            utilityResponse.setStartDate(userUtility.getStartDate());
            utilityResponse.setEndDate(userUtility.getEndDate());
            utilities.add(utilityResponse);
        }
        return utilities;
    }


}
