package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.repositories.RelativeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class RelativeRepositoryImpl implements RelativeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Relative> getRelativesByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Relative> cq = cb.createQuery(Relative.class);
        Root<Relative> root = cq.from(Relative.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("userId").get("id"), userId));
        predicates.add(cb.isTrue(root.get("isActive")));
        predicates.add(
                cb.or(
                        cb.isNull(root.get("expiredAt")),
                        cb.greaterThan(root.get("expiredAt"), new Date())
                )
        );
        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Relative getRelativeById(int id) {
        Session session = sessionFactory.getCurrentSession(); // ✅ Không dùng try
        return session.get(Relative.class, id);
    }

    @Override
    public Relative addRelative(Relative relative) {
        Session session = sessionFactory.getCurrentSession(); // ✅ Đúng rồi
        if (relative != null) {
            session.persist(relative);
            return relative;
        }
        return null;
    }

}
