package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.FeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
        root.fetch("createdBy", JoinType.LEFT);
        root.fetch("updatedBy", JoinType.LEFT);
        root.fetch("deletedBy", JoinType.LEFT);
        cq.select(root).distinct(true);
        Predicate predicates = cb.equal(root.get("isActive"), true);

        // Xử lý điều kiện tìm kiếm
        if (params != null && !params.isEmpty()) {

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
        }
        cq.where(predicates);
        return session.createQuery(cq).getResultList();
    }
    @Override
    public void updateFee(Fee fee, User user) {
        Session session = sessionFactory.getCurrentSession();
        Fee existingFee = session.get(Fee.class, fee.getId());

        if (existingFee != null) {
            if (fee.getName() != null && !fee.getName().equals(existingFee.getName())) {
                existingFee.setName(fee.getName());
            }
            if (fee.getDescription() != null && !fee.getDescription().equals(existingFee.getDescription())) {
                existingFee.setDescription(fee.getDescription());
            }
            if (fee.getType() != null && !fee.getType().equals(existingFee.getType())) {
                existingFee.setType(fee.getType());
            }
            if (fee.getPrice() != existingFee.getPrice()) {
                existingFee.setPrice(fee.getPrice());
            }
            existingFee.setUpdatedBy(user);
            existingFee.setUpdatedAt(new Date());
            session.merge(existingFee);
        }
    }
    @Override
    public Integer deleteFee(Fee f) {
        Session session = sessionFactory.getCurrentSession();
        if (f != null) {
            session.merge(f);
            return 1;
        }
        return 0;
    }

    @Override
    public Fee getFeeById(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> root = cq.from(Fee.class);

        root.fetch("createdBy", JoinType.LEFT);
        root.fetch("updatedBy", JoinType.LEFT);
        root.fetch("deletedBy", JoinType.LEFT);

        cq.select(root)
                .where(
                        cb.and(
                                cb.equal(root.get("id"), id),
                                cb.equal(root.get("isActive"), true)
                        )
                )
                .distinct(true);
        return session.createQuery(cq).uniqueResult();
    }

    //lấy những phí dịch vụ
    @Override
    public List<Fee> getUtilities(Map<String,String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> root = cq.from(Fee.class);
        root.fetch("createdBy", JoinType.LEFT);
        root.fetch("updatedBy", JoinType.LEFT);
        root.fetch("deletedBy", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("type"), Fee.FeeType.UTILITY));
        predicates.add(cb.equal(root.get("isActive"), true));
        if(params != null && !params.isEmpty()) {
            String kw = params.get("kw");
            if(kw != null && !kw.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + kw.toLowerCase() + "%"));
            }
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return session.createQuery(cq).getResultList();
    }

    //lấy những phí cố định
    @Override
    public List<Fee> getFeeOfFee() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> root = cq.from(Fee.class);
        root.fetch("createdBy", JoinType.LEFT);
        root.fetch("updatedBy", JoinType.LEFT);
        root.fetch("deletedBy", JoinType.LEFT);
        cq.select(root)
                .where(cb.and(cb.equal(root.get("type"), Fee.FeeType.FEE),
                                cb.equal(root.get("isActive"),true)))
                .distinct(true);

        return session.createQuery(cq).getResultList();
    }
}
