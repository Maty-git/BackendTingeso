package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
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

    @PostMapping("/{rutUser}")
    public ResponseEntity<Boolean> saveTool(@RequestBody Tool tool, @PathVariable String rutUser) {
        boolean result = toolService.saveTool(tool, rutUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ToolDTO>> getAllTools() {
        List<ToolDTO> response = toolService.getAllTools();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/tools")
    public ResponseEntity<List<ToolDTOnoKardex>> getTools() {
        List<ToolDTOnoKardex> tools = toolService.getAllToolsNoKardex();
        return ResponseEntity.ok(tools);
    }
    @PutMapping("/{id}/{rutUser}")
    public ResponseEntity<Void> deleteTool(@PathVariable Long id,@PathVariable String rutUser) {
        toolService.deleteToolById(id, rutUser);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/repairDebt/{id}/{rutUser}")
    public ResponseEntity<Void> repairTool(@PathVariable Long id,@PathVariable String rutUser) {
        toolService.repairTool(id, rutUser);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/repair/{id}/{rutUser}")
    public ResponseEntity<Void> repairToolNoDebt(@PathVariable Long id,@PathVariable String rutUser) {
        toolService.repairToolNoDebt(id, rutUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/for-repair")
    public ResponseEntity<List<ToolDTOnoKardex>> getToolsForRepair() {
        return  ResponseEntity.ok(toolService.getToolsForRepair());
    }

    @PutMapping("/update/{id}/{rutUser}")
    public ResponseEntity<Void> updateTool(
            @PathVariable Long id,
            @PathVariable String rutUser,
            @RequestBody Tool updatedTool) {
        toolService.updateToolByIdAndGroup(id, rutUser, updatedTool);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{name}/{category}")
    public ResponseEntity<ToolDTOnoKardex> getToolByNameAndCategory(
            @PathVariable String name,
            @PathVariable String category) {
        ToolDTOnoKardex tool = toolService.getToolByNameAndCategory(name, category);
        return ResponseEntity.ok(tool);
    }



}
