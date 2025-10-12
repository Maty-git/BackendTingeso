package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("SELECT l FROM Loan l " +
            "JOIN l.tool t " +
            "WHERE l.client = :client " +
            "AND l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE " +
            "AND t.name = :toolName " +
            "AND t.category = :toolCategory")
    List<Loan> findActiveLoansByClientAndToolAttributes(
            Client client,
            String toolName,
            com.ProyectoTingeso1.BackendProyecto1.Entities.Tool.ToolCategory toolCategory
    );
    @Query("SELECT COUNT(l) FROM Loan l " +
            "WHERE l.client = :client " +
            "AND l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE")
    long countActiveLoansByClient(Client client);

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
            "WHERE l.id = :id " +
            "AND l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE")
    LoanReturnDTO findActiveLoanById(Long id);

    @Query("SELECT l FROM Loan l " +
            "WHERE l.id = :id " +
            "AND l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE")
    Loan findActiveLoanByIdEntity(Long id);

    @Query("SELECT l FROM Loan l WHERE l.tool = :tool AND l.status = :status")
    Loan findLoanByToolAndStatus(Tool tool, Loan.LoanStatus status);

    @Query("""
       SELECT l.id AS id,
              c.name AS clientName,
              c.rut AS clientRut,
              t.name AS toolName,
              l.deliveryDate AS deliveryDate,
              l.returnDateExpected AS returnDateExpected,
              CASE WHEN l.returnDateExpected < CURRENT_TIMESTAMP THEN 'ATRASADO' ELSE 'VIGENTE' END AS status
       FROM Loan l
       JOIN l.client c
       JOIN l.tool t
       WHERE l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE
       ORDER BY l.returnDateExpected ASC
       """)
    List<ActiveLoanStatusDTO> findAllActiveLoansWithStatus();


    @Query("""
       SELECT l.id AS id,
              c.name AS clientName,
              c.rut AS clientRut,
              t.name AS toolName,
              l.deliveryDate AS deliveryDate,
              l.returnDateExpected AS returnDateExpected,
              CASE WHEN l.returnDateExpected < CURRENT_TIMESTAMP THEN 'ATRASADO' ELSE 'VIGENTE' END AS status
       FROM Loan l
       JOIN l.client c
       JOIN l.tool t
       WHERE l.status = com.ProyectoTingeso1.BackendProyecto1.Entities.Loan.LoanStatus.ACTIVE
       AND l.deliveryDate BETWEEN :startDate AND :endDate
       ORDER BY l.returnDateExpected ASC
       """)
    List<ActiveLoanStatusDTO> findActiveLoansWithStatusByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
       SELECT t.name AS toolName,
              t.category AS category,
              COUNT(l) AS totalLoans
       FROM Loan l
       JOIN l.tool t
       WHERE l.deliveryDate BETWEEN :startDate AND :endDate
       GROUP BY t.name, t.category
       ORDER BY COUNT(l) DESC
       """)
    List<ToolRankingDTO> findMostLoanedToolsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
       SELECT t.name AS toolName,
              t.category AS category,
              COUNT(l) AS totalLoans
       FROM Loan l
       JOIN l.tool t
       GROUP BY t.name, t.category
       ORDER BY COUNT(l) DESC
       """)
    List<ToolRankingDTO> findMostLoanedTools();
}
