package com.ProyectoTingeso1.BackendProyecto1.DTOs;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public interface ClientDTO {
    public Long getId();
    public String getName();
    public String getRut();
    public String getEmail();
    public String getStatus();
}