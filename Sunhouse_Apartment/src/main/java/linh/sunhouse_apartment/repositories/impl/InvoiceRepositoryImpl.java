package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.entity.DetailInvoice;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.repositories.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
                .where(
                        cb.and(
                                cb.equal(root.get("userId").get("id"), userId),
                                cb.isTrue(root.get("isActive"))
                        )
                );
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

    @Override
    public Map<Integer, BigDecimal> getRevenueStatistics(int year, String period) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Invoice> root = cq.from(Invoice.class);

        // Group theo month hoặc quarter
        Expression<Integer> groupExp;
        if ("quarter".equalsIgnoreCase(period)) {
            groupExp = cb.function("QUARTER", Integer.class, root.get("issuedDate"));
        } else {
            groupExp = cb.function("MONTH", Integer.class, root.get("issuedDate"));
        }

        // Sum totalAmount
        Expression<BigDecimal> sumExp = cb.sum(root.get("totalAmount"));

        cq.multiselect(
                        groupExp.alias("periodKey"),
                        sumExp.alias("totalRevenue")
                )
                .where(
                        cb.and(
                                cb.equal(cb.function("YEAR", Integer.class, root.get("issuedDate")), year),
                                cb.equal(root.get("status"), Invoice.Status.PAID) // chỉ lấy hóa đơn đã thanh toán
                        )
                )
                .groupBy(groupExp)
                .orderBy(cb.asc(groupExp));

        List<Tuple> results = session.createQuery(cq).getResultList();

        Map<Integer, BigDecimal> revenueStats = new LinkedHashMap<>();
        for (Tuple t : results) {
            revenueStats.put(
                    t.get("periodKey", Integer.class),
                    t.get("totalRevenue", BigDecimal.class)
            );
        }

        return revenueStats;
    }

    @Override
    public Integer isExistInvoice(int userId, int feeId) {
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        List<Invoice> invoices = findAllInvoicesByUserId(userId);
        for(Invoice i : invoices){
            Set<DetailInvoice> detailInvoiceSet = i.getDetailInvoiceSet();
            calendar.setTime(i.getIssuedDate());
            for(DetailInvoice d : detailInvoiceSet){
                if(d.getFeeId().getId() == feeId && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) ){
                    return 1;
                }
            }
        }
        return 0;
    }
}
