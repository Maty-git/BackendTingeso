package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<Kardex, Long> {
    // RF5.2 - Historial por herramienta
    @Query("SELECT k.id AS id, " +
            "k.movementType AS movementType, " +
            "k.quantity AS quantity, " +
            "k.date AS date, " +
            "k.userRut AS userRut, " +
            "k.tool.name AS toolName " +
            "FROM Kardex k " +
            "WHERE k.tool.id = :toolId " +
            "ORDER BY k.date DESC")
    List<KardexDTO> findMovementsByToolId(@Param("toolId") Long toolId);

    @Query("SELECT k.id AS id, " +
            "k.movementType AS movementType, " +
            "k.quantity AS quantity, " +
            "k.date AS date, " +
            "k.userRut AS userRut, " +
            "k.tool.name AS toolName " +
            "FROM Kardex k " +
            "WHERE k.tool.id = :toolId " +
            "AND k.date BETWEEN :startDate AND :endDate " +
            "ORDER BY k.date DESC")
    List<KardexDTO> findMovementsByToolAndDateRange(
            @Param("toolId") Long toolId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);



}
