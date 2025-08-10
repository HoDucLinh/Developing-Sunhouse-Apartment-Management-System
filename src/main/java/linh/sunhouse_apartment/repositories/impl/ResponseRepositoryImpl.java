package linh.sunhouse_apartment.repositories.impl;

import linh.sunhouse_apartment.entity.Response;
import linh.sunhouse_apartment.repositories.ResponseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ResponseRepositoryImpl implements ResponseRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Response response) {
        getCurrentSession().persist(response);
    }
}
