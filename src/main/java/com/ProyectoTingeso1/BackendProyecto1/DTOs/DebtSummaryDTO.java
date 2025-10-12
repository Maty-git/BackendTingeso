package com.ProyectoTingeso1.BackendProyecto1.DTOs;

public interface DebtSummaryDTO {
    Long getId();          // ID de la deuda
    int getAmount();       // Monto
    String getType();      // Tipo (LATE, DAMAGE, REPLACEMENT)

    String getClientRut(); // RUT del cliente
    String getClientName();// Nombre del cliente

    String getToolName();  // Nombre de la herramienta
}
