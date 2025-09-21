package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private KardexService kardexService;

    public Loan saveLoan(LoanRequestDTO loanDTO) {
        Client client = clientService.getClientByRut(loanDTO.getClientRut());
        String user = loanDTO.getUserRut();
        Tool tool = toolService.getToolById(loanDTO.getToolId());

        Loan newLoan = new Loan();
        newLoan.setReturnDateExpected(loanDTO.getReturnDateExpected());
        newLoan.setQuantity(loanDTO.getQuantity());
        newLoan.setStatus(loanDTO.getStatus());
        newLoan.setClient(client);
        newLoan.setUserRut(user);
        newLoan.setTool(tool);

        // Ahora registramos el movimiento en el kardex
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.LOAN); // CREACIÓN
        k.setQuantity(1);
        k.setUserRut(user);
        k.setTool(tool);

        kardexService.saveKardex(k);
        //cambiamos el estado a en prestamo de la herramienta
        toolService.setToolToLoaned(loanDTO.getToolId());

        return loanRepository.save(newLoan);
    }

    public List<LoanReturnDTO> getAllLoans() {
        return loanRepository.getLoanSummary();
    }
}
