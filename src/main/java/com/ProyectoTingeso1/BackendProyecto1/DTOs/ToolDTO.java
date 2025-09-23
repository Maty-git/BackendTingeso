package com.ProyectoTingeso1.BackendProyecto1.DTOs;

public interface ToolDTO {
    String getName();
    String getCategory();
    String getState();
    int getRentDailyRate();
    int getLateFee();
    int getReplacementValue();
    int getCount(); // cantidad agrupada
}
