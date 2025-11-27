package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Floor;
import linh.sunhouse_apartment.repositories.FloorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class FloorRepositoryImpl implements FloorRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Floor> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Floor ", Floor.class).getResultList();
    }

    @Override
    public Floor findFloorById(Integer id) {
        if(id != null) {
            Session session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Floor> cq = cb.createQuery(Floor.class);
            Root<Floor> root = cq.from(Floor.class);
            cq.where(cb.equal(root.get("id"), id));
            return session.createQuery(cq).getSingleResult();
        }
        return null;
    }
}
