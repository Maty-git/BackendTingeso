package com.ProyectoTingeso1.BackendProyecto1.DTOs;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;

public interface ToolDTOnoKardex {
    Long getId();
    String getName();
    Tool.ToolCategory getCategory();
    Tool.ToolState getState();
    int getQuantity();
    int getRentDailyRate();
    int getLateFee();
    int getReplacementValue();
    int getRepairCost();
    boolean isOutOfService();
}