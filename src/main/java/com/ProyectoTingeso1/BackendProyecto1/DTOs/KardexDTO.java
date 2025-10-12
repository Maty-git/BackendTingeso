package com.ProyectoTingeso1.BackendProyecto1.DTOs;

import java.time.LocalDateTime;

public interface KardexDTO {
    Long getId();
    String getMovementType();
    int getQuantity();
    LocalDateTime getDate();
    String getUserRut();
    String getToolName();
}
