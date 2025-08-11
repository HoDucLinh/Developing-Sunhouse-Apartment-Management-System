package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.repositories.FeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class FeeRepositoryImpl implements FeeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addFee(Fee fee) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(fee);
    }

    @Override
    public List<Fee> getFees(Map<String,String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> root = cq.from(Fee.class);
        cq.select(root);
        // Xử lý điều kiện tìm kiếm
        if (params != null && !params.isEmpty()) {
            var predicates = cb.conjunction(); // mặc định true

            // Tìm theo tên
            String kw = params.get("kw");
            if (kw != null && !kw.trim().isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("name")), "%" + kw.toLowerCase() + "%"));
            }

            // Tìm theo loại phí
            String type = params.get("type");
            if (type != null && !type.trim().isEmpty()) {
                try {
                    predicates = cb.and(predicates, cb.equal(root.get("type"), Fee.FeeType.valueOf(type)));
                } catch (IllegalArgumentException e) {
                    // Nếu type không hợp lệ thì bỏ qua
                }
            }

            cq.where(predicates);
        }

        return session.createQuery(cq).getResultList();
    }
}
