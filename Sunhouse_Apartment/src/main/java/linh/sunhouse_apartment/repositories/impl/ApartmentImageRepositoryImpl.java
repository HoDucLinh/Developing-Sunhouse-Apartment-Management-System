package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.ApartmentImage;
import linh.sunhouse_apartment.repositories.ApartmentImageRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ApartmentImageRepositoryImpl implements ApartmentImageRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ApartmentImage addApartmentImage(ApartmentImage apartmentImage) {
        Session session = sessionFactory.getCurrentSession();
        if(apartmentImage != null){
            session.persist(apartmentImage);
        }
        return apartmentImage;
    }

    @Override
    public List<ApartmentImage> getApartmentImages(Integer apartmentInfoId) {
        Session session = sessionFactory.getCurrentSession();
        if(apartmentInfoId != null){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentImage> query = cb.createQuery(ApartmentImage.class);
            Root<ApartmentImage> root = query.from(ApartmentImage.class);
            query.where(cb.equal(root.get("apartment_id"), apartmentInfoId));
            return session.createQuery(query).getResultList();
        }
        return null;
    }

    @Override
    public ApartmentImage getApartmentImage(Integer imageId) {
        Session session = sessionFactory.getCurrentSession();
        if(imageId != null){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentImage> query = cb.createQuery(ApartmentImage.class);
            Root<ApartmentImage> root = query.from(ApartmentImage.class);
            query.where(cb.equal(root.get("id"), imageId));
            return session.createQuery(query).getResultList().get(0);
        }
        return null;
    }

    @Override
    public ApartmentImage updateApartmentImage(ApartmentImage apartmentImage) {
        Session session = sessionFactory.getCurrentSession();
        if(apartmentImage != null){
            session.merge(apartmentImage);
        }
        return apartmentImage;
    }

    @Override
    public Integer deleteApartmentImage(ApartmentImage apartmentImage) {
        if(apartmentImage != null){
            Session session = sessionFactory.getCurrentSession();
            session.delete(apartmentImage);
            return 1;
        }
        return 0;
    }
}
