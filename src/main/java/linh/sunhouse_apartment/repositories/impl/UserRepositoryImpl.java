package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<User> getUserByUserName(String username) {
        Session session = sessionFactory.getCurrentSession();
        try {
            User user = session.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public User getUserById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    @Override
    public User saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        return user;
    }

    @Override
    public List<User> getAllUsers(Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root root = q.from(User.class);
        q.select(root);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("isActive"), true));

        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("username"), String.format("%%%s%%", kw)));
            }
            String roomNumber = params.get("room_number");
            if (roomNumber != null && !roomNumber.isEmpty()) {
                predicates.add(b.like(root.get("roomId").get("roomNumber"), String.format("%%%s%%", roomNumber)));
            }
            q.where(predicates.toArray(Predicate[]::new));

            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                q.orderBy(b.asc(root.get(orderBy)));
            }
        }

        Query query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public User editProfile(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (user != null) {
            session.merge(user);
        }
        return user;
    }

    @Override
    public int blockUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            user.setIsActive(false);
            session.merge(user);
            return 1;
        }
        return 0;
    }

}
