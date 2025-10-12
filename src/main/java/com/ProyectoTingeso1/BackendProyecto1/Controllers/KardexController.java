package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/kardex")
@CrossOrigin("*")
public class KardexController {
    @Autowired
    KardexService kardexService;

    @GetMapping("/all")
    public ResponseEntity<List<Kardex>> getAllKardex() {
        List<Kardex> listKardex = kardexService.getAllKardex();
        return ResponseEntity.ok().body(listKardex);
    }

    // RF5.2
    @GetMapping("/tool/{toolId}")
    public ResponseEntity<List<KardexDTO>> getMovementsByTool(@PathVariable Long toolId) {
        return ResponseEntity.ok(kardexService.getMovementsByTool(toolId));
    }

    // RF5.3
    // RF5.3
    @GetMapping("/tool/{toolId}/range")
    public ResponseEntity<List<KardexDTO>> getMovementsByToolAndDateRange(
            @PathVariable Long toolId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        return ResponseEntity.ok(kardexService.getMovementsByToolAndDateRange(toolId, start, end));
    }

}
