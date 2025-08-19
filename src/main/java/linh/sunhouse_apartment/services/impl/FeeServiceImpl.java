package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.repositories.FeeRepository;
import linh.sunhouse_apartment.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private Cloudinary cloudinary;

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
    public List<Fee> getUtilities() {
        return feeRepository.getUtilities();
    }

    @Override
    public List<Fee> getFeeOfFee() {
        return feeRepository.getFeeOfFee();
    }
}
