package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.QuestionOption;
import linh.sunhouse_apartment.repositories.QuestionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<Question> findQuestionsBySurveyId(int surveyId) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);

        // Fetch questionOptionSet để lấy luôn option
        root.fetch("questionOptionSet", jakarta.persistence.criteria.JoinType.LEFT);

        cq.select(root)
                .where(cb.equal(root.get("surveyId").get("id"), surveyId))
                .distinct(true);

        return sessionFactory.getCurrentSession().createQuery(cq).getResultList();
    }
    @Override
    public Question findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Question.class, id);
    }

    @Override
    public QuestionOption findQuestionOptionById(Integer id) {
        if (id == null) return null;
        return sessionFactory.getCurrentSession().get(QuestionOption.class, id);
    }


}
