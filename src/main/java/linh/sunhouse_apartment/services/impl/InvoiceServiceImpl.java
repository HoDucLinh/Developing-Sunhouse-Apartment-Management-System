package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import linh.sunhouse_apartment.dtos.request.DetailInvoiveRequest;
import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.DetailInvoiceResponse;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.*;
import linh.sunhouse_apartment.repositories.*;
import linh.sunhouse_apartment.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserUtilityRepository userUtilityRepository;

    @Override
    public InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest) {
        if (invoiceRequest == null) {
            throw new IllegalArgumentException("Invoice request cannot be null");
        }

        // Lấy User
        User u = userRepository.getUserById(invoiceRequest.getUserId());
        if (u == null) {
            throw new RuntimeException("User not found with id: " + invoiceRequest.getUserId());
        }
        //lấy các feed trong detailInvoice
        Set<Integer> requestedFeeIds = new HashSet<>();
        if (invoiceRequest.getDetails() != null) {
            for (DetailInvoiveRequest d : invoiceRequest.getDetails()) {
                if (d != null && d.getFeeId() != null) {
                    requestedFeeIds.add(d.getFeeId());
                }
            }
        }
        //danh sách utilities của user
        List<UserUtility> userUtilities = userUtilityRepository.getUserUtilityOfUser(u.getId());
        for (UserUtility userUtility : userUtilities) {
            if (requestedFeeIds.contains(userUtility.getFee().getId())
                    && userUtility.getEndDate().after(new Date())
                    && "ACTIVE".equals(userUtility.getStatus().name())) {
                throw new RuntimeException(
                        "Utility already registered and still valid for feeId: " + userUtility.getFee().getId()
                );
            }
        }
        // Tạo invoice
        Invoice invoice = new Invoice();
        invoice.setUserId(u);
        invoice.setPaymentMethod(invoiceRequest.getPaymentMethod());
        invoice.setPaymentProof(invoiceRequest.getPaymentProof()); // có thể null
        invoice.setTotalAmount(invoiceRequest.getTotalAmount());
        invoice.setAccept(false);
        invoice.setActive(true);
        invoice.setStatus(Invoice.Status.UNPAID);

        // Ngày xuất hóa đơn = hôm nay
        Date now = new Date();
        invoice.setIssuedDate(now);

        // Ngày hết hạn = 7 ngày sau
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        invoice.setDueDate(cal.getTime());

        // Lưu invoice
        Invoice savedInvoice = invoiceRepository.saveInvoice(invoice);
        //lấy danh sách detail invoice
        List<DetailInvoiceResponse> detailResponses = new ArrayList<>();

        // Lưu chi tiết hóa đơn nếu có
        if (invoiceRequest.getDetails() != null && !invoiceRequest.getDetails().isEmpty()) {
            for (DetailInvoiveRequest d : invoiceRequest.getDetails()) {
                Fee fee = feeRepository.getFeeById(d.getFeeId());
                if (fee == null) {
                    throw new RuntimeException("Fee not found with id: " + d.getFeeId());
                }
                //lưu detail invoice
                DetailInvoice detail = new DetailInvoice();
                detail.setInvoiceId(savedInvoice);
                detail.setFeeId(fee);
                detail.setAmount(d.getAmount());
                detail.setNote(d.getNote());
                detailInvoiceRepository.saveDetailInvoice(detail);
                //lưu user_utility
                UserUtility userUtility = new UserUtility();
                userUtility.setUser(u);
                userUtility.setFee(fee);
                userUtility.setStatus(UserUtility.Status.ACTIVE);
                userUtility.setStartDate(new Date());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(new Date());
                cal2.add(Calendar.MONTH, 1); // cộng thêm 1 tháng
                userUtility.setEndDate(cal.getTime());
                userUtilityRepository.addUserUtility(userUtility);

                detailResponses.add(new DetailInvoiceResponse(
                        fee.getId(),
                        fee.getName(),
                        d.getAmount(),
                        d.getNote()
                ));
            }
        }


        return new InvoiceResponse(
                savedInvoice.getId(),
                savedInvoice.getIssuedDate(),
                savedInvoice.getDueDate(),
                savedInvoice.getPaymentMethod(),
                savedInvoice.getPaymentProof(),
                savedInvoice.getTotalAmount(),
                savedInvoice.getStatus(),
                savedInvoice.isAccept(),
                savedInvoice.getUserId().getFullName(),
                detailResponses

        );
    }

    @Override
    public List<InvoiceResponse> getInvoicesByUserId(Integer userId) {
        if (userId == null || userRepository.getUserById(userId) == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<Invoice> invoices = invoiceRepository.findAllInvoicesByUserId(userId);

        return invoices.stream()
                .map(inv -> {
                    // lấy danh sách detail của invoice
                    List<DetailInvoiceResponse> details = detailInvoiceRepository.findByInvoiceId(inv.getId())
                            .stream()
                            .map(d -> new DetailInvoiceResponse(
                                    d.getFeeId().getId(),
                                    d.getFeeId().getName(),
                                    d.getAmount(),
                                    d.getNote()
                            ))
                            .toList();

                    return new InvoiceResponse(
                            inv.getId(),
                            inv.getIssuedDate(),
                            inv.getDueDate(),
                            inv.getPaymentMethod(),
                            inv.getPaymentProof(),
                            inv.getTotalAmount(),
                            inv.getStatus(),
                            inv.isAccept(),
                            inv.getUserId().getFullName(),
                            details
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void cancelInvoice(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if (invoice == null) {
            throw new RuntimeException("Invoice not found or already inactive with id: " + invoiceId);
        }
        invoice.setActive(false);
        invoiceRepository.updateInvoice(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices(Map<String, String> params) {
        return invoiceRepository.findAllInvoices(params);
    }

    @Override
    public InvoiceResponse getInvoiceDetail(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if (invoice == null) {
            throw new RuntimeException("Invoice not found with id: " + invoiceId);
        }

        List<DetailInvoiceResponse> detailResponses = detailInvoiceRepository
                .findByInvoiceId(invoice.getId())
                .stream()
                .map(d -> new DetailInvoiceResponse(
                        d.getFeeId().getId(),
                        d.getFeeId().getName(),
                        d.getAmount(),
                        d.getNote()
                ))
                .toList();

        System.out.println(detailResponses);

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getIssuedDate(),
                invoice.getDueDate(),
                invoice.getPaymentMethod(),
                invoice.getPaymentProof(),
                invoice.getTotalAmount(),
                invoice.getStatus(),
                invoice.isAccept(),
                invoice.getUserId().getFullName(),
                detailResponses
        );
    }


    @Override
    public void createInvoicesForAllRoomHeads(Integer feeId) {
        // Lấy danh sách trưởng phòng
        List<User> roomHeads = userRepository.getAllRoomHead();

        // Lấy thông tin phí
        Fee fee = feeRepository.getFeeById(feeId);
        if (fee == null) {
            throw new RuntimeException("Fee not found with id: " + feeId);
        }

        for (User u : roomHeads) {
            // Khởi tạo Invoice mới hoàn toàn
            Invoice invoice = new Invoice();
            invoice.setUserId(u);
            invoice.setPaymentMethod(Invoice.PAYMENT_METHOD.TRANSFER);   // mặc định CASH
            invoice.setPaymentProof(null);      // chưa có minh chứng
            invoice.setTotalAmount(u.getRoomId().getRentPrice().add(BigDecimal.valueOf(fee.getPrice())));
            invoice.setAccept(false);
            invoice.setActive(true);
            invoice.setStatus(Invoice.Status.UNPAID);

            // Ngày xuất = hôm nay
            Date now = new Date();
            invoice.setIssuedDate(now);

            // Ngày hết hạn = +7 ngày
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            invoice.setDueDate(cal.getTime());

            // Lưu Invoice (hàm save của repo chỉ persist đối tượng mới)
            Invoice savedInvoice = invoiceRepository.saveInvoice(invoice);

            // Tạo chi tiết hóa đơn
            DetailInvoice detail = new DetailInvoice();
            detail.setInvoiceId(savedInvoice);
            detail.setFeeId(fee);
            detail.setAmount(BigDecimal.valueOf(fee.getPrice()));
            detail.setNote("Hóa đơn tiền nhà " + u.getRoomId().getRoomNumber());

            detailInvoiceRepository.saveDetailInvoice(detail);
        }
    }

    @Override
    public void changeAccept(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if (invoice == null) {
            throw new RuntimeException("Invoice not found or already inactive with id: " + invoiceId);
        }
        invoice.setAccept(true);
        invoice.setStatus(Invoice.Status.PAID);
        invoiceRepository.updateInvoice(invoice);
    }

    @Override
    public void uploadProof(Integer invoiceId, MultipartFile file) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        if (invoice == null) {
            throw new RuntimeException("Invoice not found or already inactive with id: " + invoiceId);
        }
        if (invoice.getPaymentProof() == null) {
            try {
                Map res = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                invoice.setPaymentProof(res.get("secure_url").toString());
                invoiceRepository.updateInvoice(invoice);
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public Map<Integer, BigDecimal> getRevenueStatistics(int year, String period) {
        return invoiceRepository.getRevenueStatistics(year, period);
    }

}
