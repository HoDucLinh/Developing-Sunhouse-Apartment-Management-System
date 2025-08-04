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

/**
 *
 * @author ADMIN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback implements Serializable {

    public enum FeedbackStatus {
        PENDING,
        RESOLVED
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    @Size(max = 8)
    @Column(name = "status")
    private FeedbackStatus status;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User userId;
}
