package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.Invoice;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InvoiceService {

    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest);
    List<InvoiceResponse> getInvoicesByUserId(Integer userId);
    void cancelInvoice(Integer invoiceId);
    List<Invoice> getAllInvoices(Map<String,String> params);
    InvoiceResponse getInvoiceDetail(Integer invoiceId);
    void createInvoicesForAllRoomHeads(Integer feeId);
    void changeAccept(Integer invoiceId);
    void uploadProof(Integer invoiceId, MultipartFile file);
    Map<Integer, BigDecimal> getRevenueStatistics(int year, String period);

}
