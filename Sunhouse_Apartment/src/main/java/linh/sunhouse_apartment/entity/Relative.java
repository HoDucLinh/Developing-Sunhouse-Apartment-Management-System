/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linh.sunhouse_apartment.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "relative")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name = "Relative.findAll", query = "SELECT r FROM Relative r"),
    @NamedQuery(name = "Relative.findById", query = "SELECT r FROM Relative r WHERE r.id = :id"),
    @NamedQuery(name = "Relative.findByFullName", query = "SELECT r FROM Relative r WHERE r.fullName = :fullName"),
    @NamedQuery(name = "Relative.findByPhone", query = "SELECT r FROM Relative r WHERE r.phone = :phone"),
    @NamedQuery(name = "Relative.findByRelationship", query = "SELECT r FROM Relative r WHERE r.relationship = :relationship"),
    @NamedQuery(name = "Relative.findByCreatedAt", query = "SELECT r FROM Relative r WHERE r.createdAt = :createdAt")})
public class Relative implements Serializable {

    public enum EnumRelationship{
        OWNER,
        PARENT
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "full_name")
    private String fullName;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(name = "relationship")
    private EnumRelationship relationship;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "relativeId")
    @JsonIgnore
    private Set<Card> cardSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User userId;
}
