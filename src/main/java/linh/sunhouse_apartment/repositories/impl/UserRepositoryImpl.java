package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.exceptions.AppException;
import linh.sunhouse_apartment.exceptions.ErrorCode;
import linh.sunhouse_apartment.repositories.UserRepository;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private LocalSessionFactoryBean factory;
    @Override
    public Optional<User> getUserByUserName(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return (Optional<User>) q.getSingleResult();
    }
}
