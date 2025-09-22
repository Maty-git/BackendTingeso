package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Services.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Debts")
@CrossOrigin("*")
public class DebtController {
    @Autowired
    private DebtService debtService;

    @PostMapping("/save")
    public ResponseEntity<Debt> createDebt(@RequestBody Debt debt) {
        Debt newDebt = debtService.saveDebt(debt);
        return ResponseEntity.ok(newDebt);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Debt>> getAllDebt() {
        return ResponseEntity.ok(debtService.getAllDebt());
    }
}
