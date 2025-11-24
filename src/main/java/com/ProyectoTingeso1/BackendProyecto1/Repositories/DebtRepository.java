package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    Debt findByLoan(Loan loan);
    @Query("SELECT d FROM Debt d WHERE d.id = :id")
    Debt findDebtById(@Param("id") Long id);
    @Query("SELECT d FROM Debt d WHERE d.loan = :loan AND d.paid = false")
    Debt findUnpaidDebtByLoan(@Param("loan") Loan loan);

    @Query("SELECT d FROM Debt d WHERE d.client = :client AND d.paid = false")
    List<Debt> findUnpaidDebtsByClient(@Param("client") Client client);

    @Query("SELECT d.id AS id, " +
            "d.amount AS amount, " +
            "d.type AS type, " +
            "c.rut AS clientRut, " +
            "c.name AS clientName, " +
            "t.name AS toolName " +
            "FROM Debt d " +
            "JOIN d.client c " +
            "JOIN d.loan l " +
            "JOIN l.tool t " +
            "WHERE d.paid = false")
    List<DebtSummaryDTO> findAllUnpaidDebtSummaries();

    @Query("SELECT d.id AS id, " +
            "d.amount AS amount, " +
            "d.type AS type, " +
            "c.rut AS clientRut, " +
            "c.name AS clientName, " +
            "t.name AS toolName " +
            "FROM Debt d " +
            "JOIN d.client c " +
            "JOIN d.loan l " +
            "JOIN l.tool t " +
            "WHERE d.paid = false AND d.type = 'LATE'")
    List<DebtSummaryDTO> findAllUnpaidDebtWhereTypeIsLate();

    @Query("SELECT DISTINCT c.id AS id, c.name AS name, c.rut AS rut, c.email AS email, c.status AS status " +
            "FROM Debt d " +
            "JOIN d.client c " +
            "WHERE d.paid = false AND d.type = 'LATE' " +
            "AND CAST(d.debtDate AS date) BETWEEN CAST(:startDate AS date) AND CAST(:endDate AS date)")
    List<ClientDTO> findClientsWithLateDebtsInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
