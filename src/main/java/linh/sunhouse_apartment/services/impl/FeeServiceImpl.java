package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.repositories.FeeRepository;
import linh.sunhouse_apartment.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Override
    public void addFee(Fee fee) {
        if (fee == null) {
            throw new IllegalArgumentException("Fee object must not be null");
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
}
