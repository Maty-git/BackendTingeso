package com.ProyectoTingeso1.BackendProyecto1.DTOs;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;

import java.time.LocalDateTime;

public interface LoanReturnDTO {
    Long getId();
    LocalDateTime getReturnDateExpected();
    LocalDateTime getDeliveryDate();
    String getUserRut();

    // Client
    String getClientName();
    String getClientRut();

    // Tool
    String getToolName();
}
