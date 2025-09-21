package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;

    @Autowired
    KardexService kardexRepository;
    @Autowired
    private KardexService kardexService;

    public boolean saveTool(Tool tool, String rutUser) {
        int quantity = tool.getQuantity();
        int i;

        for (i = 0; i < quantity; i++) {
            Tool newTool = new Tool();
            newTool.setName(tool.getName());
            newTool.setCategory(tool.getCategory());
            newTool.setState(tool.getState());
            newTool.setQuantity(1);
            newTool.setRentDailyRate(tool.getRentDailyRate());
            newTool.setLateFee(tool.getLateFee());
            newTool.setReplacementValue(tool.getReplacementValue());
            newTool.setRepairCost(tool.getRepairCost());

            // Guardamos primero la herramienta
            Tool savedTool = toolRepository.save(newTool);

            // Ahora registramos el movimiento en el kardex
            Kardex k = new Kardex();
            k.setMovementType(Kardex.MovementType.CREATION); // CREACIÓN
            k.setQuantity(1);
            k.setUserRut(rutUser);
            k.setTool(savedTool);

            kardexService.saveKardex(k);
        }

        return i == quantity;
    }
    public List<ToolDTO> getAllTools() {
        return toolRepository.getToolSummary();
    }

    public List<ToolDTOnoKardex> getAllToolsNoKardex() {
        return  toolRepository.getAllToolsNoKardex();
    }

    public Tool getToolById(Long id){
        return toolRepository.findById(id).get();
    }

    public void deleteToolById(Long id, String rutUser) {
        Tool tool = toolRepository.getById(id);
        tool.setOutOfService(true);
        tool.setState(Tool.ToolState.OUT_OF_SERVICE);
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.CANCELLATION); // CREACIÓN
        k.setQuantity(1);
        k.setUserRut(rutUser);
        k.setTool(tool);
        kardexService.saveKardex(k);
        toolRepository.save(tool);
    }

    public void setToolToLoaned (Long id) {
        Tool tool = toolRepository.getById(id);
        tool.setState(Tool.ToolState.LOANED);
    }
}
