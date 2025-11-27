package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.UserUtility;
import linh.sunhouse_apartment.repositories.UserUtilityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Repository
@Transactional
public class UserUtilityRepositoryImpl implements UserUtilityRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<UserUtility> getUserUtilityOfUser(Integer userId) {
        Session s =  sessionFactory.getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<UserUtility> cq = cb.createQuery(UserUtility.class);
        Root<UserUtility> root = cq.from(UserUtility.class);
        root.fetch("fee", jakarta.persistence.criteria.JoinType.LEFT);
        cq.select(root).where(cb.equal(root.get("user").get("id"), userId));

        return s.createQuery(cq).getResultList();

    }

    @Override
    public UserUtility addUserUtility(UserUtility userUtility) {
        Session s =  sessionFactory.getCurrentSession();
        if (userUtility != null){
            s.persist(userUtility);
            return userUtility;
        }
        return null;
    }

    @Override
    public UserUtility getUserUtilityById(Integer Id) {
        Session s = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<UserUtility> cq = cb.createQuery(UserUtility.class);
        Root<UserUtility> root = cq.from(UserUtility.class);

        // WHERE id = :id
        cq.select(root).where(cb.equal(root.get("id"), id));

        return s.createQuery(cq).uniqueResult();
    }
}
