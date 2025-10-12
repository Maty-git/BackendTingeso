package com.ProyectoTingeso1.BackendProyecto1.DTOs;

import java.time.LocalDateTime;

public interface ActiveLoanStatusDTO {
    Long getId();
    String getClientName();
    String getClientRut();
    String getToolName();
    LocalDateTime getDeliveryDate();
    LocalDateTime getReturnDateExpected();
    String getStatus(); // "VIGENTE" o "ATRASADO"
}
