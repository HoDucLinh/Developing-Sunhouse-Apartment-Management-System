package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.entity.DetailSurvey;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Survey;
import linh.sunhouse_apartment.repositories.SurveyRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        cq.select(root)
                .where(cb.equal(root.get("id"), id));

        return getCurrentSession().createQuery(cq).uniqueResult();
    }


    @Override
    public List<Survey> findAll(Map<String, String> params) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> root = cq.from(Survey.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();

        // Nếu có điều kiện tìm kiếm theo title
        if (params != null && !params.isEmpty()) {
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                Survey.SurveyType surveyType = Survey.SurveyType.valueOf(status.toUpperCase());
                predicates.add(cb.equal(root.get("type"), surveyType));
            }
            if (!predicates.isEmpty()) {
                cq.where(predicates.toArray(Predicate[]::new));
            }

        }

        return getCurrentSession().createQuery(cq).getResultList();
    }

    @Override
    public List<Survey> getSurveysNotCompletedByUser(int userId, String title) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> surveyRoot = cq.from(Survey.class);

        List<Predicate> predicates = new ArrayList<>();

        // Subquery: chọn survey_id trong detail_survey mà user đã COMPLETED
        Subquery<Integer> sub = cq.subquery(Integer.class);
        Root<DetailSurvey> dsRoot = sub.from(DetailSurvey.class);
        sub.select(dsRoot.get("survey").get("id"))
                .where(
                        cb.equal(dsRoot.get("user").get("id"), userId),
                        cb.equal(dsRoot.get("status"), DetailSurvey.Status.COMPLETED)
                );

        // Thêm điều kiện NOT IN
        predicates.add(cb.not(surveyRoot.get("id").in(sub)));

        // Nếu có truyền title thì thêm điều kiện LIKE
        if (title != null && !title.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(surveyRoot.get("title")), "%" + title.toLowerCase() + "%"));
        }

        // Gộp tất cả predicate
        cq.select(surveyRoot).where(cb.and(predicates.toArray(new Predicate[0])));

        return getCurrentSession().createQuery(cq).getResultList();
    }

}
