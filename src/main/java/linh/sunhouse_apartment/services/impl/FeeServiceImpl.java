package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.dtos.response.FeeResponse;
import linh.sunhouse_apartment.entity.DetailInvoice;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.DetailInvoiceRepository;
import linh.sunhouse_apartment.repositories.FeeRepository;
import linh.sunhouse_apartment.repositories.InvoiceRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
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
    private InvoiceRepository invoiceRepository;

    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

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
    public List<Fee> getUtilities(Map<String,String> params) {
        return feeRepository.getUtilities(params);
    }

    @Override
    public List<Fee> getFeeOfFee() {
        return feeRepository.getFeeOfFee();
    }

    @Override
    public List<FeeResponse> getUtilitiesOfUser(Integer userId) {
        User u = userRepository.getUserById(userId);
        if (u == null)
            throw new RuntimeException("Not found user " + userId);
        List<Invoice> invoices = invoiceRepository.findAllInvoicesByUserId(userId);
        List<FeeResponse> utilities = new ArrayList<>();
        for (Invoice i : invoices) {
            List<DetailInvoice> details = detailInvoiceRepository.findByInvoiceId(i.getId());
            for (DetailInvoice d : details) {
                Fee f = feeRepository.getFeeById(d.getFeeId().getId());
                if(f.getType().equals(Fee.FeeType.UTILITY)) {
                    FeeResponse feeResponse = new FeeResponse(f.getName(), f.getDescription(), f.getPrice(), i.getIssuedDate());
                    utilities.add(feeResponse);
                }
            }
        }
        return utilities;
    }


}
