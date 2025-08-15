package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.DetailInvoice;
import linh.sunhouse_apartment.repositories.DetailInvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class DetailInvoiceRepositoryImpl implements DetailInvoiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DetailInvoice saveDetailInvoice(DetailInvoice detailInvoice) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(detailInvoice);
        return detailInvoice;
    }

    @Override
    public List<DetailInvoice> findByInvoiceId(Integer invoiceId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<DetailInvoice> cq = cb.createQuery(DetailInvoice.class);
        Root<DetailInvoice> root = cq.from(DetailInvoice.class);

        cq.select(root)
                .where(cb.equal(root.get("invoiceId").get("id"), invoiceId));

        return session.createQuery(cq).getResultList();
    }
}
