package com.ProyectoTingeso1.BackendProyecto1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "debts")
@Data  // <-- genera getters, setters, toString, equals y hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private int amount;
    private String type; // LATE, DAMAGE, REPLACEMENT
    private boolean paid;

    @ManyToOne(targetEntity = Loan.class)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
