package com.ProyectoTingeso1.BackendProyecto1.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tools")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    public enum ToolState {
        AVAILABLE,
        LOANED,
        UNDER_REPAIR,
        OUT_OF_SERVICE
    }
    public enum ToolCategory {
        MANUAL,
        ELECTRICAL,
        CONSTRUCTION,
        CUTTING,
        CARPENTRY,
        WELDING,
        GARDENING,
        MEASUREMENT,
        SCAFFOLDING,
        MACHINERY,
        SAFETY,
        ACCESSORIES
    }
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToolCategory category;
    @Enumerated(EnumType.STRING)
    private ToolState state;
    private int quantity;


    private int rentDailyRate;     // tarifa diaria de arriendo
    private int lateFee;     // tarifa diaria de multa por atraso
    @Column(nullable = false)
    private int replacementValue;  // valor de reposición


    //relaciones
    @OneToMany(targetEntity =  Kardex.class,fetch = FetchType.LAZY)
    private List<Kardex> kardexMovements = new ArrayList<>();

    @OneToMany(targetEntity = Loan.class,fetch = FetchType.LAZY)
    private List<Loan> loanMovement = new ArrayList<>();
}
