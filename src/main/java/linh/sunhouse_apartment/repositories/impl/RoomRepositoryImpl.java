package linh.sunhouse_apartment.repositories.impl;


import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.repositories.RoomRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RoomRepositoryImpl implements RoomRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Room> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Room", Room.class)
                .getResultList();
    }

    @Override
    public Room findById(Integer id) {
        return sessionFactory.getCurrentSession().get(Room.class, id);
    }

    @Override
    public List<Room> findByFloorId(Integer floorId) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT r FROM Room r WHERE r.floor.id = :floorId", Room.class)
                .setParameter("floorId", floorId)
                .getResultList();
    }

}
