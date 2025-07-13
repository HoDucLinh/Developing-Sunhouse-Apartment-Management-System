package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
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
}
