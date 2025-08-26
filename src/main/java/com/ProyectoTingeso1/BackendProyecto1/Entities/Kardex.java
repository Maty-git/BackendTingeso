package com.ProyectoTingeso1.BackendProyecto1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "kardexs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kardex {
    public enum MovementType {
        LOAN,
        RETURN,
        CANCELLATION,
        ADJUSTMENT
    }
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MovementType movementType; //(PRÉSTAMO, DEVOLUCIÓN, BAJA, AJUSTE
    private int quantity;
    @Column(nullable = false, updatable = false)
    private LocalDateTime date;
    // with this hibernate capture a date when user was created
    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }

    //relaciones
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    @ManyToOne(targetEntity = Tool.class)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

}
