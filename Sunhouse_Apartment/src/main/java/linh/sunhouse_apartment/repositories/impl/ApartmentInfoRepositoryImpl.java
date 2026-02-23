package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.ApartmentInfo;
import linh.sunhouse_apartment.repositories.ApartmentInfoRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ApartmentInfoRepositoryImpl implements ApartmentInfoRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ApartmentInfo addApartmentInfo(ApartmentInfo apartment) {
        Session  session = this.sessionFactory.getCurrentSession();
        session.persist(apartment);
        return apartment;
    }


    @Override
    public ApartmentInfo getApartmentInfo(Integer apartmentInfoId) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ApartmentInfo> cq = cb.createQuery(ApartmentInfo.class);
        Root<ApartmentInfo> root = cq.from(ApartmentInfo.class);
        root.fetch("images", JoinType.LEFT);
        cq.select(root).distinct(true).where(cb.equal(root.get("id"), apartmentInfoId));
        List<ApartmentInfo> results = session.createQuery(cq).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public ApartmentInfo updateApartmentInfo(ApartmentInfo apartment) {
        Session session = this.sessionFactory.getCurrentSession();
        if (apartment != null) {
            session.merge(apartment);
        }
        return apartment;
    }
}
