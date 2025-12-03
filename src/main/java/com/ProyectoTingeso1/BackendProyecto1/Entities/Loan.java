package com.ProyectoTingeso1.BackendProyecto1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        UNPAID_DEBT,
        TOOL_BROKE
    }

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime returnDateExpected;

    private LocalDateTime realReturnDate;

    private int quantity;

    private int price;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime deliveryDate;

    @PrePersist
    protected void onCreate() {
        this.deliveryDate = LocalDateTime.now();
    }

    @Column(nullable = false)
    private String userRut;

    private Boolean delay = false;

    // relaciones
    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(targetEntity = Tool.class)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @OneToMany(mappedBy = "loan", targetEntity = Debt.class, fetch = FetchType.LAZY)
    private List<Debt> debts;
}
