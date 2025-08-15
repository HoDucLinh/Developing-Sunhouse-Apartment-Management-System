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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    public enum PAYMENT_METHOD{
        TRANSFER,
        CASH
    }

    public enum Status{
        PAID,
        UNPAID
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "issued_date")
    @Temporal(TemporalType.DATE)
    private Date issuedDate;
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Size(max = 8)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PAYMENT_METHOD paymentMethod;
    @Lob
    @Size(max = 65535)
    @Column(name = "payment_proof")
    private String paymentProof;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_accept")
    private boolean isAccept;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    @Size(max = 6)
    @Column(name = "status")
    private Status status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceId")
    @JsonIgnore
    private Set<DetailInvoice> detailInvoiceSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
}
