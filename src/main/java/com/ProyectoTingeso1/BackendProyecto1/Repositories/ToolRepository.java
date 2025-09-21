package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
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
            "WHERE t.state <> com.ProyectoTingeso1.BackendProyecto1.Entities.Tool.ToolState.OUT_OF_SERVICE " +
            "GROUP BY t.name, t.category, t.state, t.rentDailyRate, t.lateFee, t.replacementValue")
    List<ToolDTO> getToolSummary();

    @Query("SELECT t.id AS id, " +
            "t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.quantity AS quantity, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "t.repairCost AS repairCost, " +
            "t.outOfService AS outOfService " +
            "FROM Tool t " +
            "WHERE t.state <> com.ProyectoTingeso1.BackendProyecto1.Entities.Tool.ToolState.OUT_OF_SERVICE")
    List<ToolDTOnoKardex> getAllToolsNoKardex();
}

