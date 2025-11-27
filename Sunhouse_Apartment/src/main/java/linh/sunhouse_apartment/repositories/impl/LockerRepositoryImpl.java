package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.LockerRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class LockerRepositoryImpl implements LockerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Locker addLocker(User u) {
        if(u == null)
            return null;
        Locker locker = new Locker();
        locker.setId(u.getId());
        locker.setUser(u);
        getCurrentSession().persist(locker);
        return locker;
    }

    @Override
    public Locker getLockerByID(int userId) {
        return getCurrentSession().get(Locker.class, userId);
    }


    @Override
    public List<Locker> getAllLockers(String keyword) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Locker> cq = cb.createQuery(Locker.class);
        Root<Locker> root = cq.from(Locker.class);

        // Join với bảng User (locker.user)
        Join<Object, Object> userJoin = root.join("user");

        List<Predicate> predicates = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(userJoin.get("fullName")), pattern));
        }

        cq.select(root);

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return session.createQuery(cq).getResultList();
    }
}
