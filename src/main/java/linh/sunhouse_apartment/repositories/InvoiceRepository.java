package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Invoice;

import java.util.List;
import java.util.Map;

public interface InvoiceRepository {
    Invoice saveInvoice (Invoice invoice);
    List<Invoice> findAllInvoices(Map<String,String> params);
    List<Invoice> findAllInvoicesByUserId(Integer userId);
    Integer updateInvoice(Invoice invoice);
    Invoice findInvoiceById(Integer id);
}
