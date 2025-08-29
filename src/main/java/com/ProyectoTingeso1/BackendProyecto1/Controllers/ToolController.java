package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin("*")
public class ToolController {
    @Autowired
    private ToolService toolService;

    @PostMapping("/")
    public ResponseEntity<Boolean> saveTool(@RequestBody Tool tool) {
        boolean result = toolService.saveTool(tool);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ToolDTO>> getAllTools() {
        List<ToolDTO> response = toolService.getAllTools();
        return ResponseEntity.ok(response);
    }
}
