package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.LoanController;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }
}
