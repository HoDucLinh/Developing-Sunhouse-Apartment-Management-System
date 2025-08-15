package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.DetailInvoice;

import java.util.List;

public interface DetailInvoiceRepository {
    DetailInvoice saveDetailInvoice(DetailInvoice detailInvoice);
    List<DetailInvoice> findByInvoiceId(Integer invoiceId);
}
