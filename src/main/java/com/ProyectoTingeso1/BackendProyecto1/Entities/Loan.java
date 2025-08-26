package com.ProyectoTingeso1.BackendProyecto1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        UNPAID_FINE,
        REPLACEMENT
    }
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime returnDateExpected;

    private LocalDateTime realReturnDate;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime deliveryDate;
    // with this hibernate capture a date when user was created
    @PrePersist
    protected void onCreate() {
        this.deliveryDate = LocalDateTime.now();
    }

    private int repairCost;
    //relaciones
    @ManyToOne(targetEntity =  Client.class)
    @JoinColumn(name = "client_id",  nullable = false)
    private Client client;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(targetEntity = Tool.class)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;
}
