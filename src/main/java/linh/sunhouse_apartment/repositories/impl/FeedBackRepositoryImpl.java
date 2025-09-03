package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.FeedBackRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class FeedBackRepositoryImpl implements FeedBackRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Feedback createFeedback(Feedback feedback) {
        Session session = sessionFactory.getCurrentSession();
        if(feedback != null){
            session.persist(feedback);
            return feedback;
        }
        return null;
    }

    @Override
    public List<Feedback> findAllFeedback(Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Feedback> q = b.createQuery(Feedback.class);
        Root root = q.from(Feedback.class);
        q.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("content"), String.format("%%%s%%", kw)));
            }
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(b.like(root.get("userId").get("fullName"), String.format("%%%s%%", name)));
            }
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                try {
                    Feedback.FeedbackStatus st = Feedback.FeedbackStatus.valueOf(status.toUpperCase());
                    predicates.add(b.equal(root.get("status"), st));
                } catch (IllegalArgumentException e) {
                    // Nếu status không hợp lệ thì bỏ qua
                }
            }
            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(Predicate[]::new));
            }
            String sort = params.get("sort");
            if ("oldest".equalsIgnoreCase(sort)) {
                q.orderBy(b.asc(root.get("createdAt")));
            } else {
                // mặc định newest
                q.orderBy(b.desc(root.get("createdAt")));
            }

        }

        Query query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<Feedback> findAllFeedbackByUserId(Integer userId, Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Feedback> cq = cb.createQuery(Feedback.class);
        Root<Feedback> root = cq.from(Feedback.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("userId").get("id"), userId));
        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(cb.like(root.get("content"), String.format("%%%s%%", kw)));
            }
            cq.where(predicates.toArray(Predicate[]::new));
        }
        Query query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public boolean deleteFeedbackById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Feedback feedback = session.get(Feedback.class, id);
        if (feedback != null) {
            session.remove(feedback);
            return true;
        }
        return false;
    }

    @Override
    public Feedback updateFeedback(Feedback updatedFeedback) {
        Session session = sessionFactory.getCurrentSession();
        Feedback existing = session.get(Feedback.class, updatedFeedback.getId());
        if (existing != null) {
            existing.setContent(updatedFeedback.getContent());
            session.merge(existing);
            return existing;
        }
        return null;
    }

    @Override
    public Feedback findFeedbackById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Feedback.class, id);
    }

    @Override
    public void updateStatus(int id, Feedback.FeedbackStatus status) {
        Session session = sessionFactory.getCurrentSession();
        Feedback fb = session.get(Feedback.class, id);
        if (fb != null) {
            fb.setStatus(status);
            session.merge(fb);
        }
    }
}
