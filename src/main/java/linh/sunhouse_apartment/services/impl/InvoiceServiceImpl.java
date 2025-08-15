package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.DetailInvoiveRequest;
import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.DetailInvoice;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.DetailInvoiceRepository;
import linh.sunhouse_apartment.repositories.FeeRepository;
import linh.sunhouse_apartment.repositories.InvoiceRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

        // Lưu chi tiết hóa đơn nếu có
        if (invoiceRequest.getDetails() != null && !invoiceRequest.getDetails().isEmpty()) {
            for (DetailInvoiveRequest d : invoiceRequest.getDetails()) {
                Fee fee = feeRepository.getFeeById(d.getFeeId());
                if (fee == null) {
                    throw new RuntimeException("Fee not found with id: " + d.getFeeId());
                }

                DetailInvoice detail = new DetailInvoice();
                detail.setInvoiceId(savedInvoice);
                detail.setFeeId(fee);
                detail.setAmount(d.getAmount());
                detail.setNote(d.getNote());
                detailInvoiceRepository.saveDetailInvoice(detail);
            }
        }

        // Trả về DTO, không trả entity để tránh lazy load error
        return new InvoiceResponse(
                savedInvoice.getId(),
                savedInvoice.getIssuedDate(),
                savedInvoice.getDueDate(),
                savedInvoice.getPaymentMethod(),
                savedInvoice.getPaymentProof(),
                savedInvoice.getTotalAmount(),
                savedInvoice.getStatus(),
                savedInvoice.getUserId().getId()
        );
    }

}
