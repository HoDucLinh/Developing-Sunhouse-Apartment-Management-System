/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linh.sunhouse_apartment.repositories;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "room_head")
@NamedQueries({
    @NamedQuery(name = "RoomHead.findAll", query = "SELECT r FROM RoomHead r"),
    @NamedQuery(name = "RoomHead.findByRoomId", query = "SELECT r FROM RoomHead r WHERE r.roomId = :roomId")})
public class RoomHead implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "room_id")
    private Integer roomId;
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Room room;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private User userId;

    public RoomHead() {
    }

    public RoomHead(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomHead)) {
            return false;
        }
        RoomHead other = (RoomHead) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apartment_management.pojo.RoomHead[ roomId=" + roomId + " ]";
    }
    
}
