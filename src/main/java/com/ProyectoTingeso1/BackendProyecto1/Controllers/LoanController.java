package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/save")
    public boolean saveLoan(@RequestBody LoanRequestDTO loan) {
        boolean  loanNew = loanService.saveLoan(loan);
        return loanNew;
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanReturnDTO>> getAllLoan() {
        List<LoanReturnDTO> loanList = loanService.getAllLoans();
        return ResponseEntity.ok(loanList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanReturnDTO> getLoanById(@PathVariable("id") Long id) {
        LoanReturnDTO loan = loanService.getLoanReturnDTOById(id);
        return  ResponseEntity.ok(loan);
    }

    @PutMapping("/return/{id}/{userRut}/{bool}")
    public boolean returnLoan(@PathVariable("id") Long id,@PathVariable("userRut") String userRut, @PathVariable("bool") boolean bool) {
        return loanService.returnLoan(id,userRut,bool);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ActiveLoanStatusDTO>> getActiveLoansWithStatus() {
        return ResponseEntity.ok(loanService.getActiveLoansWithStatus());
    }

    @GetMapping("/active/range")
    public ResponseEntity<List<ActiveLoanStatusDTO>> getActiveLoansWithStatusByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        return ResponseEntity.ok(loanService.getActiveLoansWithStatusByDateRange(start, end));
    }

    @GetMapping("/ranking/range")
    public ResponseEntity<List<ToolRankingDTO>> getMostLoanedToolsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        return ResponseEntity.ok(loanService.getMostLoanedToolsByDateRange(start, end));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ToolRankingDTO>> getMostLoanedTools(){
        return ResponseEntity.ok(loanService.getMostLoanedTools());
    }
}
