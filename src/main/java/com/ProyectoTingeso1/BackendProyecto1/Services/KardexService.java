package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KardexService {
    @Autowired
    KardexRepository kardexRepository;

    public ResponseEntity<Object> saveKardex(Kardex kardex) {
        Kardex savedKardex = kardexRepository.save(kardex);
        return new ResponseEntity<>(savedKardex, HttpStatus.OK);
    }

    public List<Kardex> getAllKardex() {
        return kardexRepository.findAll();
    }

    public List<KardexDTO> getMovementsByTool(Long toolId) {
        return kardexRepository.findMovementsByToolId(toolId);
    }

    public List<KardexDTO> getMovementsByToolAndDateRange(Long toolId, LocalDateTime startDate, LocalDateTime endDate) {
        return kardexRepository.findMovementsByToolAndDateRange(toolId, startDate, endDate);
    }


}
