package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT " +
            "l.id AS id, " +
            "l.returnDateExpected AS returnDateExpected, " +
            "l.deliveryDate AS deliveryDate, " +
            "l.userRut AS userRut, " +
            "c.name AS clientName, " +
            "c.rut AS clientRut, " +
            "t.name AS toolName " +
            "FROM Loan l " +
            "JOIN l.client c " +
            "JOIN l.tool t " +
            "WHERE l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE")
    List<LoanReturnDTO> getLoanSummary();
    List<Loan> findByStatus(Loan.LoanStatus status);
}
