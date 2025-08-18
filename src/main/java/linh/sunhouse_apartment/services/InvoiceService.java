package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.DetailInvoiceResponse;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.Invoice;

import java.util.List;
import java.util.Map;

public interface InvoiceService {

    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest);
    List<InvoiceResponse> getInvoicesByUserId(Integer userId);
    void cancelInvoice(Integer invoiceId);
    List<Invoice> getAllInvoices(Map<String,String> params);
    InvoiceResponse getInvoiceDetail(Integer invoiceId);
}
