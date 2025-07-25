package linh.sunhouse_apartment.repositories.impl;

import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.repositories.QuestionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class QuestionRepositoryImpl implements QuestionRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Question question) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(question);
    }
}
