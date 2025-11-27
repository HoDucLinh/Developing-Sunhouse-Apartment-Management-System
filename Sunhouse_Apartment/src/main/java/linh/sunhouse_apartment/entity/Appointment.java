package linh.sunhouse_apartment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "appointment", indexes = {
        @Index(name = "BOD_id", columnList = "BOD_id")
})
public class Appointment {
    public enum AppointmentStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "BOD_id", nullable = true)
    private User bod;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Lob
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "fullName", nullable = false)
    private String fullName;

}