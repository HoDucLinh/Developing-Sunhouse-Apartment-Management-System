/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linh.sunhouse_apartment.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "package")
@NamedQueries({
    @NamedQuery(name = "Package.findAll", query = "SELECT p FROM Package p"),
    @NamedQuery(name = "Package.findById", query = "SELECT p FROM Package p WHERE p.id = :id"),
    @NamedQuery(name = "Package.findByName", query = "SELECT p FROM Package p WHERE p.name = :name"),
    @NamedQuery(name = "Package.findByStatus", query = "SELECT p FROM Package p WHERE p.status = :status"),
    @NamedQuery(name = "Package.findByCreatedAt", query = "SELECT p FROM Package p WHERE p.createdAt = :createdAt")})
public class Package implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "image")
    private String image;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "received_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;
    @JoinColumn(name = "locker_id", referencedColumnName = "id")
    @ManyToOne
    private Locker lockerId;
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User senderId;
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User receiverId;

}
