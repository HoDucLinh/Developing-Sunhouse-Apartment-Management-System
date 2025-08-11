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
    @Override
    public void updateFee(Fee fee) {
        Session session = sessionFactory.getCurrentSession();
        Fee existingFee = session.get(Fee.class, fee.getId());

        if (existingFee != null) {
            // Chỉ cập nhật nếu giá trị mới khác giá trị cũ
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

            session.merge(existingFee);
        }
    }
    @Override
    public int deleteFee(int id) {
        Session session = sessionFactory.getCurrentSession();
        Fee f = session.get(Fee.class, id);
        if (f != null) {
            session.remove(f);
            return 1;
        }
        return 0;
    }

    @Override
    public Fee getFeeById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Fee.class, id);
    }
    @Override
    public List<Fee> getUtilities() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> root = cq.from(Fee.class);

        cq.select(root)
                .where(cb.equal(root.get("type"), Fee.FeeType.UTILITY));

        return session.createQuery(cq).getResultList();
    }
}
