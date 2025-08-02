package linh.sunhouse_apartment.repositories.impl;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.entity.Appointment;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.AppointmentRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Appointment addAppointment(Appointment appointment) {
        Session s = this.factory.getObject().getCurrentSession();
        if(appointment != null) {
            s.persist(appointment);
            return appointment;
        }
        return null;
    }

    @Override
    public List<Appointment> getAppointments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> q = b.createQuery(Appointment.class);
        Root root = q.from(Appointment.class);
        q.select(root);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("status"), Appointment.AppointmentStatus.PENDING));

        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("fullName"), String.format("%%%s%%", kw)));
            }
            String email = params.get("email");
            if (email != null && !email.isEmpty()) {
                predicates.add(b.like(root.get("email"), String.format("%%%s%%", email)));
            }
            q.where(predicates.toArray(Predicate[]::new));

        }

        Query query = session.createQuery(q);
        return query.getResultList();
    }
}
