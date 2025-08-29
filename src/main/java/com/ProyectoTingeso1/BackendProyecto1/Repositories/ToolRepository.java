package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    @Query("SELECT t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "COUNT(t) AS count " +
            "FROM Tool t " +
            "GROUP BY t.name, t.category, t.state, t.rentDailyRate, t.lateFee, t.replacementValue")
    List<ToolDTO> getToolSummary();
}
