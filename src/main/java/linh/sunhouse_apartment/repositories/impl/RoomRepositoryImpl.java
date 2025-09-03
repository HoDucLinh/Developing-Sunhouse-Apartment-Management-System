package linh.sunhouse_apartment.repositories.impl;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.dtos.response.UserResponse;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.entity.RoomHead;
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

        session.createQuery("DELETE FROM RoomHead WHERE room.id = :roomId")
                .setParameter("roomId", roomId)
                .executeUpdate();

        Room room = session.get(Room.class, roomId);
        User user = session.get(User.class, newHeadId);

        RoomHead newHead = new RoomHead();
        newHead.setRoomId(room.getId());
        newHead.setRoom(room);
        newHead.setUserId(user);

        session.persist(newHead);
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

    private RoomResponse mapToRoomResponse(Session session, CriteriaBuilder cb, Room room) {
        RoomResponse dto = new RoomResponse();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloorId(room.getFloor() != null ? room.getFloor().getId() : null);
        dto.setMaxPeople(room.getMaxPeople());
        dto.setArea(room.getArea());
        dto.setDescription(room.getDescription());

        CriteriaQuery<User> userQuery = cb.createQuery(User.class);
        Root<User> userRoot = userQuery.from(User.class);
        userQuery.select(userRoot).where(cb.equal(userRoot.get("roomId"), room));
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

        CriteriaQuery<RoomHead> headQuery = cb.createQuery(RoomHead.class);
        Root<RoomHead> headRoot = headQuery.from(RoomHead.class);
        headQuery.select(headRoot).where(cb.equal(headRoot.get("room"), room));
        List<RoomHead> headResult = session.createQuery(headQuery).getResultList();

        if (!headResult.isEmpty() && headResult.get(0).getUserId() != null) {
            dto.setRoomHeadId(headResult.get(0).getUserId().getId());
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

}
