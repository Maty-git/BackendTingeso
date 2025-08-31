package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Entities.User;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private UserService userService;

    public Loan saveLoan(LoanRequestDTO loanDTO) {
        Client client = clientService.getClientByRut(loanDTO.getClientRut());
        User user = userService.getUserByRut(loanDTO.getUserRut());
        Tool tool = toolService.getToolById(loanDTO.getToolId());

        Loan newLoan = new Loan();
        newLoan.setReturnDateExpected(loanDTO.getReturnDateExpected());
        newLoan.setQuantity(loanDTO.getQuantity());
        newLoan.setStatus(loanDTO.getStatus());
        newLoan.setClient(client);
        newLoan.setUser(user);
        newLoan.setTool(tool);

        return loanRepository.save(newLoan);
    }

}
