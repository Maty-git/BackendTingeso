package com.ProyectoTingeso1.BackendProyecto1.DTOs;


import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class LoanRequestDTO {
    private String clientRut;
    private String userRut;
    private Long toolId;
    private int quantity;
    private LocalDateTime returnDateExpected;
    private Loan.LoanStatus status;
}

