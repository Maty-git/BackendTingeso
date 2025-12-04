package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.*;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private KardexService kardexService;
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private ClientRepository clientRepository;

    public boolean saveLoan(LoanRequestDTO loanDTO) {
        Client client = clientService.getClientByRut(loanDTO.getClientRut());


        if ("RESTRICTED".equals(client.getStatus())) {
            return false;
        }


        long prestamosActivos = loanRepository.countActiveLoansByClient(client);
        if (prestamosActivos >= 5) {
            return false;
        }


        Tool tool = toolService.getToolById(loanDTO.getToolId());
        List<Loan> similarLoans = loanRepository.findActiveLoansByClientAndToolAttributes(
                client, tool.getName(), tool.getCategory());
        if (!similarLoans.isEmpty()) {
            return false;
        }
        String user = loanDTO.getUserRut();

        Loan newLoan = new Loan();
        newLoan.setReturnDateExpected(loanDTO.getReturnDateExpected());
        newLoan.setQuantity(loanDTO.getQuantity());
        newLoan.setStatus(loanDTO.getStatus());
        newLoan.setClient(client);
        newLoan.setUserRut(user);
        newLoan.setTool(tool);
        newLoan.setPrice(loanDTO.getPrice());


        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.LOAN); // CREACIÓN
        k.setQuantity(1);
        k.setUserRut(user);
        k.setTool(tool);

        kardexService.saveKardex(k);

        toolService.setToolToLoaned(loanDTO.getToolId());

        loanRepository.save(newLoan);
        return true;
    }

    public List<LoanReturnDTO> getAllLoans() {
        return loanRepository.getLoanSummary();
    }

    // para cuando no tiene daños
    @Transactional
    public boolean returnLoan(Long id, String userRut, boolean damage) {
        // 1. Buscar el préstamo activo
        Loan loan = loanRepository.findActiveLoanByIdEntity(id);
        if (loan == null) {
            return false; // no existe o ya fue devuelto
        }

        // 2. Verificar si hay deuda por atraso y marcarla como pagada
        Debt debt = debtRepository.findUnpaidDebtByLoan(loan);
        if (debt != null) {
            debt.setPaid(true);
            debtRepository.save(debt);
        }

        // 3. Verificar si el cliente sigue teniendo deudas pendientes
        Client client = loan.getClient();
        List<Debt> unpaidDebts = debtRepository.findUnpaidDebtsByClient(client);
        if (unpaidDebts.isEmpty()) {
            client.setStatus("ACTIVE");
            clientRepository.save(client);
        }

        // 4. Actualizar estado y fecha de devolución

        loan.setRealReturnDate(LocalDateTime.now());

        // 5. Actualizar stock de la herramienta
        // si tiene daños manda a revisar
        Tool tool = loan.getTool();
        if (damage) {
            tool.setState(Tool.ToolState.UNDER_REPAIR);
            loan.setStatus(Loan.LoanStatus.UNPAID_DEBT);
        } else {
            tool.setState(Tool.ToolState.AVAILABLE);
            loan.setStatus(Loan.LoanStatus.RETURNED);
        }
        toolRepository.save(tool);

        // 6. Registrar movimiento en el kardex
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.RETURN);
        k.setQuantity(loan.getQuantity());
        k.setUserRut(userRut);
        k.setTool(tool);
        kardexService.saveKardex(k);

        // 7. Guardar cambios en Loan
        loanRepository.save(loan);

        return true;
    }

    public LoanReturnDTO getLoanReturnDTOById(Long id) {
        return loanRepository.findActiveLoanById(id);
    }

    public List<ActiveLoanStatusDTO> getActiveLoansWithStatus() {
        return loanRepository.findAllActiveLoansWithStatus();
    }

    public List<ActiveLoanStatusDTO> getActiveLoansWithStatusByDateRange(LocalDateTime start, LocalDateTime end) {
        return loanRepository.findActiveLoansWithStatusByDateRange(start, end);
    }

    public List<ToolRankingDTO> getMostLoanedToolsByDateRange(LocalDateTime start, LocalDateTime end) {
        return loanRepository.findMostLoanedToolsByDateRange(start, end);
    }

    public List<ToolRankingDTO> getMostLoanedTools() {
        return loanRepository.findMostLoanedTools();
    }
}
