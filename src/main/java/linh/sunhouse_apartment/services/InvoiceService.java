package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest);
    List<InvoiceResponse> getInvoicesByUserId(Integer userId);
    void cancelInvoice(Integer invoiceId);
}
