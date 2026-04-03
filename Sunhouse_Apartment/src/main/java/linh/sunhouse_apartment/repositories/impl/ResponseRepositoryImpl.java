package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Response;
import linh.sunhouse_apartment.repositories.ResponseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<Response> getResponses(Question question) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Response> cq = cb.createQuery(Response.class);
        Root<Response> root = cq.from(Response.class);

        // 👇 JOIN FETCH optionId và userId
        root.fetch("optionId", JoinType.LEFT);
        root.fetch("userId", JoinType.LEFT);

        cq.select(root).where(
                cb.equal(root.get("questionId").get("id"), question.getId())
        );

        return getCurrentSession().createQuery(cq).getResultList();
    }
}
