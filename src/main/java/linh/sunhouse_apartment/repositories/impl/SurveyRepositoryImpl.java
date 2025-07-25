package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SurveyRepositoryImpl implements SurveyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public void save(Survey survey) {
        if(survey == null)
            return;
        getCurrentSession().persist(survey);
    }

    @Override
    public Survey findById(int id) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> root = cq.from(Survey.class);

        // Fetch questionSet để tránh lazy load
        root.fetch("questionSet", JoinType.LEFT);

        cq.select(root)
                .where(cb.equal(root.get("id"), id))
                .distinct(true);

        Survey survey = getCurrentSession().createQuery(cq).uniqueResult();

        // ✅ Khởi tạo thủ công questionOptionSet cho từng Question
        if (survey != null && survey.getQuestionSet() != null) {
            for (Question question : survey.getQuestionSet()) {
                Hibernate.initialize(question.getQuestionOptionSet());
            }
        }

        return survey;
    }


    @Override
    public List<Survey> findAll(String title) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> root = cq.from(Survey.class);

        // Nếu có điều kiện tìm kiếm theo title
        if (title != null && !title.trim().isEmpty()) {
            cq.where(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        return getCurrentSession().createQuery(cq).getResultList();
    }
}
