package linh.sunhouse_apartment.repositories.impl;


import jakarta.persistence.criteria.*;
import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.dtos.response.UserResponse;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.RoomRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class RoomRepositoryImpl implements RoomRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<RoomResponse> findAll(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Room> cq = cb.createQuery(Room.class);
        Root<Room> root = cq.from(Room.class);

        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("roomNumber")), pattern));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0])).distinct(true);

        List<Room> roomList = session.createQuery(cq).getResultList();

        List<RoomResponse> results = new ArrayList<>();
        for (Room room : roomList) {
            RoomResponse dto = mapToRoomResponse(session, cb, room);
            results.add(dto);
        }
        return results;
    }

    @Override
    public RoomResponse findRoomWithUsers(Integer roomId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        Room room = session.get(Room.class, roomId);
        return mapToRoomResponse(session, cb, room);
    }

    @Override
    public void updateRoomHead(Integer roomId, Integer newHeadId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Room> update = cb.createCriteriaUpdate(Room.class);
        Root<Room> root = update.from(Room.class);

        User newHead = session.get(User.class, newHeadId);

        update.set(root.get("headUser"), newHead);
        update.where(cb.equal(root.get("id"), roomId));

        session.createMutationQuery(update).executeUpdate();

    }

    @Override
    public List<User> getUsersByRoomId(Room room) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> userQuery = cb.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        userQuery.select(userRoot).where(cb.equal(userRoot.get("roomId"), room));
        return session.createQuery(userQuery).getResultList();
    }

    @Override
    public Room update(Room room) {
        if (room != null) {
            sessionFactory.getCurrentSession().merge(room);
            return room;
        }
        return null;
    }

    @Override
    public Room addRoom(Room room) {
        sessionFactory.getCurrentSession().persist(room);
        return room;
    }

    @Override
    public Room findRoomWithRoomNumber(String roomNumber) {
        if (roomNumber != null) {
            Session session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Room> cq = cb.createQuery(Room.class);
            Root<Room> root = cq.from(Room.class);
            cq.select(root).where(cb.equal(root.get("roomNumber"), roomNumber));
            List<Room> results = session.createQuery(cq).getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
        return null;
    }

    private RoomResponse mapToRoomResponse(Session session, CriteriaBuilder cb, Room room) {
        RoomResponse dto = new RoomResponse();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloorId(room.getFloor() != null ? room.getFloor().getId() : null);
        dto.setMaxPeople(room.getMaxPeople());
        dto.setArea(room.getArea());
        dto.setDescription(room.getDescription());
        dto.setRent_price(room.getRentPrice());

        CriteriaQuery<User> userQuery = cb.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        userQuery.select(userRoot).where(
                cb.and(cb.equal(userRoot.get("roomId"), room),
                                        cb.equal(userRoot.get("isActive"),true)
                )
        );
        List<User> users = session.createQuery(userQuery).getResultList();

        List<UserResponse> userDTOs = users.stream().map(user -> {
            UserResponse u = new UserResponse();
            u.setId(user.getId());
            u.setUsername(user.getUsername());
            u.setFullName(user.getFullName());
            u.setEmail(user.getEmail());
            u.setPhone(user.getPhone());
            return u;
        }).collect(Collectors.toList());
        dto.setUsers(userDTOs);
        int available = room.getMaxPeople() - (users != null ? users.size() : 0);
        dto.setAvailableSlots(available);

        if (room.getHeadUser() != null) {
            dto.setRoomHeadId(room.getHeadUser().getId());
        }

        return dto;
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

    @Override
    public List<User> getAllRoomHead() {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<Room> roomRoot = cq.from(Room.class);
        Join<Room, User> headJoin = roomRoot.join("headUser");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNotNull(roomRoot.get("headUser")));
        predicates.add(cb.equal(headJoin.get("isActive"), true));

        cq.select(headJoin).distinct(true)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.asc(headJoin.get("fullName")));

        return session.createQuery(cq).getResultList();
    }

}
