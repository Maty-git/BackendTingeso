package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DebtService {
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;

    public Debt saveDebt(Debt debt) {
        return debtRepository.save(debt);
    }
    public List<Debt> getAllDebt() {return debtRepository.findAll();}
    public Debt getDebt(Long id) {
        Loan loan = loanRepository.findActiveLoanByIdEntity(id);
        return debtRepository.findUnpaidDebtByLoan(loan);
    }
    public boolean payDebt(Long id) {
        Debt debt = debtRepository.findDebtById(id);
        if (debt == null) {
            return false;
        }
        debt.setPaid(true);
        debtRepository.save(debt);
        Loan loan = debt.getLoan();
        Client client = loan.getClient();
        List<Debt> unpaidDebts = debtRepository.findUnpaidDebtsByClient(client);
        if (unpaidDebts.isEmpty()) {
            client.setStatus("ACTIVE");
            clientRepository.save(client);
        }
        
        return true;
    }

    public List<DebtSummaryDTO> getUnpaidDebtsSummary() {
        return debtRepository.findAllUnpaidDebtSummaries();
    }

    public List<ClientDTO> getUnpaidDebtsWhereTypeIsLate() {
        List<DebtSummaryDTO> debts = debtRepository.findAllUnpaidDebtWhereTypeIsLate();
        List<ClientDTO> clients = new ArrayList<>(); // ✅ Lista modificable
        for (DebtSummaryDTO debt : debts) {
            ClientDTO client = clientRepository.findClientDTOByRut(debt.getClientRut());
            if (client != null && !clients.contains(client)) {
                clients.add(client);
            }
        }
        return clients;
    }

    public List<ClientDTO> getLateClientsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return debtRepository.findClientsWithLateDebtsInDateRange(startDate, endDate);
    }



}
