package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/save")
    public ResponseEntity<Loan> saveLoan(@RequestBody LoanRequestDTO loan) {
        Loan  loanNew = loanService.saveLoan(loan);
        return ResponseEntity.ok(loanNew);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoan() {
        List<Loan> loanList = loanService.getAllLoans();
        return ResponseEntity.ok(loanList);
    }

}
