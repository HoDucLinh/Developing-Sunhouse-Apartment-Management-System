package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.CardRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CardRepositoryImpl implements CardRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Card addCard(Card c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(c);
        return c;
    }

    public List<Card> getCardsByUserId(int userId) {
        Session session = factory.getObject().getCurrentSession();
        Query<Card> query = session.createQuery(
                "FROM Card c WHERE c.userId.id = :userId AND c.status = :status", Card.class);
        query.setParameter("userId", userId);
        query.setParameter("status", "active");
        return query.getResultList();
    }

    @Override
    public boolean deleteCard(int cardId) {
        Session session = factory.getObject().getCurrentSession();
        Card c = session.get(Card.class, cardId);
        if (c != null) {
            session.remove(c);
            return true;
        }
        return false;
    }
    @Override
    public List<Card> getAllCards(String keyword) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Card> query = builder.createQuery(Card.class);
        Root<Card> root = query.from(Card.class);
        Join<Card, User> userJoin = root.join("userId");

        if (keyword != null && !keyword.trim().isEmpty()) {
            Predicate predicate = builder.like(builder.lower(userJoin.get("fullName")), "%" + keyword + "%");
            query.select(root).where(predicate);
        } else {
            query.select(root);
        }

        return session.createQuery(query).getResultList();
    }
}
