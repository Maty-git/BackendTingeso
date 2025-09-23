package com.ProyectoTingeso1.BackendProyecto1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String rut;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    private String status;
    //relaciones
    @OneToMany(mappedBy = "client",targetEntity = Loan.class, fetch = FetchType.LAZY)
    private List<Loan> loans;
    @OneToMany(mappedBy = "client", targetEntity = Debt.class, fetch = FetchType.LAZY)
    private List<Debt> debts;
}
