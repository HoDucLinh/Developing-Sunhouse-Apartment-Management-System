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

        Predicate predicate = cb.equal(root.get("userId").get("id"), userId);
        cq.select(root).where(predicate);

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
