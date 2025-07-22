package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.repositories.PackageRepository;
import org.hibernate.Session;
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
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public Package addPackage(Package p) {
        if(p != null){
            getCurrentSession().persist(p);
        }
        return p;
    }

    @Override
    public int changeStatusPackage(int packageID, Package.Status newStatus) {
        Package pkg = getCurrentSession().get(Package.class, packageID);
        if(pkg != null){
            pkg.setStatus(newStatus);
            getCurrentSession().merge(pkg);
            return 1;
        }
        return 0;
    }

    @Override
    public int deletePackage(int packageID) {
        Package pkg = getCurrentSession().get(Package.class, packageID);
        if(pkg != null){
            getCurrentSession().remove(pkg);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Package> findAllPackagesById(Locker l, String kw) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
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

        return getCurrentSession().createQuery(cq).getResultList();
    }

}
