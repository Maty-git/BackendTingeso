package com.ProyectoTingeso1.BackendProyecto1.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public enum Role {
        ADMIN,
        EMPLOYEE
    }
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String rut;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;
    // with this hibernate capture a date when user was created
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    //relaciones
    @OneToMany(targetEntity = Kardex.class,fetch =  FetchType.LAZY)
    private List<Kardex> kardexMovement =  new ArrayList<>();

    @OneToMany(targetEntity = Loan.class,fetch =  FetchType.LAZY)
    private List<Loan> loanMovement =  new ArrayList<>();
}
