package linh.sunhouse_apartment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "room", indexes = {
        @Index(name = "floor_id", columnList = "floor_id")
})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @NotNull
    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;

    @NotNull
    @Column(name = "area", nullable = false)
    private Double area;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "rent_price", precision = 12, scale = 2)
    private BigDecimal rentPrice;

}