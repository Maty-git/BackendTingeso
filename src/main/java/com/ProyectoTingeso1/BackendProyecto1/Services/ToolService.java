package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.*;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    DebtRepository debtRepository;
    @Autowired
    ClientRepository clientRepository;
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

    @Transactional
    public void deleteToolById(Long id, String rutUser) {
        Tool tool = toolRepository.getById(id);

        // 1. Marcar herramienta fuera de servicio
        tool.setOutOfService(true);
        tool.setState(Tool.ToolState.OUT_OF_SERVICE);

        // 2. Registrar movimiento en Kardex
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.CANCELLATION);
        k.setQuantity(1);
        k.setUserRut(rutUser);
        k.setTool(tool);
        kardexService.saveKardex(k);

        toolRepository.save(tool);

        // 3. Buscar préstamo activo con UNPAID_DEBT asociado a esta herramienta
        Loan loan = loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.UNPAID_DEBT);
        if (loan != null) {
            // 4. Crear deuda para el cliente
            Debt debt = new Debt();
            debt.setLoan(loan);
            debt.setClient(loan.getClient());
            debt.setType("TOOL_BROKE");
            debt.setAmount(tool.getReplacementValue());
            debt.setPaid(false);
            debtRepository.save(debt);

            // 5. Actualizar estado del préstamo
            loan.setStatus(Loan.LoanStatus.TOOL_BROKE);
            loanRepository.save(loan);
            // actualizar estado de cliente a RESTRICTED ya que tiene una nueva deuda
            Client client = loan.getClient();
            client.setStatus("RESTRICTED");
            clientRepository.save(client);
        }
    }


    @Transactional
    public void repairTool(Long id, String rutUser) {
        Tool tool = toolRepository.getById(id);

        // 1. Restaurar herramienta como disponible
        tool.setOutOfService(false);
        tool.setState(Tool.ToolState.AVAILABLE);

        // 2. Registrar movimiento en Kardex
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.REPAIR);
        k.setQuantity(1);
        k.setUserRut(rutUser);
        k.setTool(tool);
        kardexService.saveKardex(k);

        toolRepository.save(tool);

        // 3. Buscar préstamo que quedó marcado como UNPAID_DEBT
        Loan loan = loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.UNPAID_DEBT);
        if (loan != null) {
            // 4. Crear deuda de reparación
            Debt debt = new Debt();
            debt.setLoan(loan);
            debt.setClient(loan.getClient());
            debt.setType("REPAIR");
            debt.setAmount(tool.getRepairCost()); // 🔹 deberías definir un campo repairCost en Tool
            debt.setPaid(false);
            debtRepository.save(debt);

            // 5. Actualizar cliente a RESTRICTED porque tiene nueva deuda
            Client client = loan.getClient();
            client.setStatus("RESTRICTED");
            clientRepository.save(client);
        }
    }

    @Transactional
    public void repairToolNoDebt(Long id, String rutUser) {
        Tool tool = toolRepository.getById(id);

        // 1. Restaurar herramienta como disponible
        tool.setOutOfService(false);
        tool.setState(Tool.ToolState.AVAILABLE);

        // 2. Registrar movimiento en Kardex
        Kardex k = new Kardex();
        k.setMovementType(Kardex.MovementType.REPAIR);
        k.setQuantity(1);
        k.setUserRut(rutUser);
        k.setTool(tool);
        kardexService.saveKardex(k);

        toolRepository.save(tool);

    }

    public List<ToolDTOnoKardex> getToolsForRepair() {
        return toolRepository.getToolsForRepair();
    }

    public void setToolToLoaned (Long id) {
        Tool tool = toolRepository.getById(id);
        tool.setState(Tool.ToolState.LOANED);
    }

    @Transactional
    public void updateToolByIdAndGroup(Long id, String rutUser, Tool updatedTool) {
        Tool baseTool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        // 1. Buscar todas las herramientas que tengan mismos atributos que la base (menos el id)
        List<Tool> toolsToUpdate = toolRepository.findByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue(
                baseTool.getName(),
                baseTool.getCategory(),
                baseTool.getRentDailyRate(),
                baseTool.getLateFee(),
                baseTool.getReplacementValue()
        );

        // 2. Actualizar cada herramienta
        for (Tool t : toolsToUpdate) {
            t.setName(updatedTool.getName());
            t.setCategory(updatedTool.getCategory());
            t.setState(updatedTool.getState());
            t.setRentDailyRate(updatedTool.getRentDailyRate());
            t.setLateFee(updatedTool.getLateFee());
            t.setReplacementValue(updatedTool.getReplacementValue());
            t.setRepairCost(updatedTool.getRepairCost());
            t.setOutOfService(updatedTool.isOutOfService());

            Kardex k = new Kardex();
            k.setMovementType(Kardex.MovementType.UPDATE);
            k.setQuantity(1);
            k.setUserRut(rutUser);
            k.setTool(t);
            kardexService.saveKardex(k);
        }
        toolRepository.saveAll(toolsToUpdate);

        // 3. Kardex (opcional: puedes registrar actualización si lo deseas)

    }

    public ToolDTOnoKardex getToolByNameAndCategory(String toolName, String category) {
        Tool.ToolCategory toolCategory = Tool.ToolCategory.valueOf(category.toUpperCase());
        List<ToolDTOnoKardex> listTool = toolRepository.findOneByNameAndCategory(toolName, toolCategory);
        for (ToolDTOnoKardex tool : listTool) {
            return tool;
        }
        return null;
    }

}
