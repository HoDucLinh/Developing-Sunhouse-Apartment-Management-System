package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InvoiceRepository {
    Invoice saveInvoice (Invoice invoice);
    List<Invoice> findAllInvoices(Map<String,String> params);
    List<Invoice> findAllInvoicesByUserId(Integer userId);
    Integer updateInvoice(Invoice invoice);
    Invoice findInvoiceById(Integer id);
    Map<Integer, BigDecimal> getRevenueStatistics(int year, String period);
    Integer isExistInvoice(int userId, int feeId);
}
