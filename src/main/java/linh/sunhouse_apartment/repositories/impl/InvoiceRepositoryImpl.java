package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class InvoiceRepositoryImpl implements InvoiceRepository {

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if(invoice != null) {
            Session session = sessionFactory.getCurrentSession();
            session.persist(invoice);
            return invoice;
        }
        return null;
    }

    @Override
    public List<Invoice> findAllInvoices(Map<String,String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> q = b.createQuery(Invoice.class);
        Root root = q.from(Invoice.class);
        q.select(root);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("isActive"), true));

        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("userId").get("fullName"), String.format("%%%s%%", kw)));
            }
            q.where(predicates.toArray(Predicate[]::new));

        }

        Query query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<Invoice> findAllInvoicesByUserId(Integer userId) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Invoice> cq = cb.createQuery(Invoice.class);
        Root<Invoice> root = cq.from(Invoice.class);
        cq.select(root)
                .where(cb.equal(root.get("userId").get("id"), userId));

        return sessionFactory.getCurrentSession().createQuery(cq).getResultList();
    }

    @Override
    public Integer updateInvoice(Invoice invoice) {
        Session session = sessionFactory.getCurrentSession();
        if(invoice != null) {
            session.merge(invoice);
            return 1;
        }
        return 0;
    }

    @Override
    public Invoice findInvoiceById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Invoice> cq = cb.createQuery(Invoice.class);
        Root<Invoice> root = cq.from(Invoice.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        predicates.add(cb.equal(root.get("id"), id));
        cq.where(predicates.toArray(new Predicate[0]));

        // Thực thi query
        List<Invoice> results = session.createQuery(cq).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
