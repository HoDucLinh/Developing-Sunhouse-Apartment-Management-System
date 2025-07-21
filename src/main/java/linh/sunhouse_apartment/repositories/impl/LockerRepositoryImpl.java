package linh.sunhouse_apartment.repositories.impl;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.LockerRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Locker> getAllLockers() {
        Query query = getCurrentSession().createQuery("FROM Locker", Locker.class);
        return query.getResultList();
    }
}
