package linh.sunhouse_apartment.repositories.impl;

import linh.sunhouse_apartment.entity.DetailSurvey;
import linh.sunhouse_apartment.repositories.DetailSurveyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DetailSurveyRepositoryImpl implements DetailSurveyRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DetailSurvey save(DetailSurvey detailSurvey) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(detailSurvey);
        return detailSurvey;
    }
}
