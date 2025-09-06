package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.repositories.PackageRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class PackageRepositoryImpl implements PackageRepository {

    @Autowired
    private SessionFactory factory;

    @Override
    public Package addPackage(Package p) {
        Session session = factory.getCurrentSession();
        if(p != null){
            session.persist(p);
        }
        return p;
    }

    @Override
    public Integer update (Package pk) {
        Session session = factory.getCurrentSession();
        if(pk != null){
            session.update(pk);
            return 1;
        }
        return 0;
    }



    @Override
    public int deletePackage(int packageID) {
        Package pkg = factory.getCurrentSession().get(Package.class, packageID);
        if(pkg != null){
            factory.getCurrentSession().remove(pkg);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Package> findAllPackagesById(Locker l, String kw) {
        CriteriaBuilder cb = factory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Package> cq = cb.createQuery(Package.class);
        Root<Package> root = cq.from(Package.class);

        List<Predicate> predicates = new ArrayList<>();

        // Điều kiện: locker = l
        predicates.add(cb.equal(root.get("lockerId"), l));

        // Điều kiện: name LIKE %kw%
        if (kw != null && !kw.trim().isEmpty()) {
            String pattern = "%" + kw.trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("name")), pattern));
        }

        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return factory.getCurrentSession().createQuery(cq).getResultList();
    }

    @Override
    public Package findPackageById(int packageID) {
        CriteriaBuilder cb = factory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Package> cq = cb.createQuery(Package.class);
        Root<Package> root = cq.from(Package.class);

        cq.select(root).where(cb.equal(root.get("id"), packageID));

        List<Package> results = factory.getCurrentSession().createQuery(cq).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
