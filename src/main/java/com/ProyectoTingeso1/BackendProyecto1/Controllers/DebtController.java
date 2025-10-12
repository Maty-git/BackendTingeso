package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Services.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getDebtById(@PathVariable Long id) {
        Debt debt = debtService.getDebt(id);
        return ResponseEntity.ok(debt);
    }

    @PutMapping("/pay/{id}")
    public boolean payDebt(@PathVariable Long id) {
        return debtService.payDebt(id);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<DebtSummaryDTO>> getUnpaidDebtsSummary() {
        return ResponseEntity.ok(debtService.getUnpaidDebtsSummary());
    }
    @GetMapping("/late")
    public ResponseEntity<List<ClientDTO>> getUnpaidDebtsWhereTypeIsLate() {
        return ResponseEntity.ok(debtService.getUnpaidDebtsWhereTypeIsLate());
    }
    @GetMapping("/late/range")
    public ResponseEntity<List<ClientDTO>> getLateClientsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Convertir LocalDate a LocalDateTime al inicio y final del día
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<ClientDTO> clients = debtService.getLateClientsByDateRange(start, end);
        return ResponseEntity.ok(clients);
    }


}
