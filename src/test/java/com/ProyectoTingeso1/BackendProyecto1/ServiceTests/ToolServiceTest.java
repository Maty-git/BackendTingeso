package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import com.ProyectoTingeso1.BackendProyecto1.Services.ToolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private KardexService kardexService;

    @InjectMocks
    private ToolService toolService;

    private Tool tool;
    private Client client;
    private Loan loan;

    @BeforeEach
    void setUp() {
        tool = new Tool();
        tool.setId(1L);
        tool.setName("Martillo");
        tool.setCategory(Tool.ToolCategory.MANUAL);
        tool.setState(Tool.ToolState.AVAILABLE);
        tool.setQuantity(3);
        tool.setRentDailyRate(1000);
        tool.setLateFee(500);
        tool.setReplacementValue(5000);
        tool.setRepairCost(1000);
        tool.setOutOfService(false);

        client = new Client();
        client.setId(1L);
        client.setStatus("ACTIVE");

        loan = new Loan();
        loan.setId(1L);
        loan.setClient(client);
        loan.setTool(tool);
        loan.setStatus(Loan.LoanStatus.UNPAID_DEBT);
    }

    @Test
    void whenSaveTool_thenCreateMultipleToolsAndKardexEntries() {
        // Given
        when(toolRepository.save(any(Tool.class))).thenReturn(tool);
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());

        // When
        boolean result = toolService.saveTool(tool, "11111111-1");

        // Then
        assertThat(result).isTrue();
        verify(toolRepository, times(3)).save(any(Tool.class));
        verify(kardexService, times(3)).saveKardex(any());
    }

    @Test
    void whenGetAllTools_thenReturnToolDTOList() {
        // Given
        ToolDTO toolDTO = mock(ToolDTO.class);
        when(toolRepository.getToolSummary()).thenReturn(Arrays.asList(toolDTO));

        // When
        List<ToolDTO> result = toolService.getAllTools();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(toolRepository, times(1)).getToolSummary();
    }

    @Test
    void whenGetAllToolsNoKardex_thenReturnToolList() {
        // Given
        ToolDTOnoKardex toolDTO = mock(ToolDTOnoKardex.class);
        when(toolRepository.getAllToolsNoKardex()).thenReturn(Arrays.asList(toolDTO));

        // When
        List<ToolDTOnoKardex> result = toolService.getAllToolsNoKardex();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(toolRepository, times(1)).getAllToolsNoKardex();
    }

    @Test
    void whenGetToolById_thenReturnTool() {
        // Given
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        // When
        Tool result = toolService.getToolById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Martillo");
        verify(toolRepository, times(1)).findById(1L);
    }

    @Test
    void whenDeleteToolById_withUnpaidDebtLoan_thenCreateDebtAndUpdateLoan() {
        // Given
        when(toolRepository.getById(1L)).thenReturn(tool);
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.UNPAID_DEBT)).thenReturn(loan);
        when(debtRepository.save(any())).thenReturn(new Debt());
        when(loanRepository.save(any())).thenReturn(loan);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        toolService.deleteToolById(1L, "11111111-1");

        // Then
        assertThat(tool.isOutOfService()).isTrue();
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.OUT_OF_SERVICE);
        verify(debtRepository, times(1)).save(any(Debt.class));
        verify(loanRepository, times(1)).save(loan);
        assertThat(loan.getStatus()).isEqualTo(Loan.LoanStatus.TOOL_BROKE);
        assertThat(client.getStatus()).isEqualTo("RESTRICTED");
    }

    @Test
    void whenDeleteToolById_withoutLoan_thenOnlyMarkAsOutOfService() {
        // Given
        when(toolRepository.getById(1L)).thenReturn(tool);
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.UNPAID_DEBT)).thenReturn(null);

        // When
        toolService.deleteToolById(1L, "11111111-1");

        // Then
        assertThat(tool.isOutOfService()).isTrue();
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.OUT_OF_SERVICE);
        verify(debtRepository, never()).save(any(Debt.class));
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenRepairTool_withUnpaidDebtLoan_thenCreateRepairDebt() {
        // Given
        tool.setState(Tool.ToolState.UNDER_REPAIR);
        when(toolRepository.getById(1L)).thenReturn(tool);
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.UNPAID_DEBT)).thenReturn(loan);
        when(debtRepository.save(any())).thenReturn(new Debt());
        when(clientRepository.save(any())).thenReturn(client);

        // When
        toolService.repairTool(1L, "11111111-1");

        // Then
        assertThat(tool.isOutOfService()).isFalse();
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.AVAILABLE);
        verify(debtRepository, times(1)).save(any(Debt.class));
        assertThat(client.getStatus()).isEqualTo("RESTRICTED");
    }

    @Test
    void whenRepairToolNoDebt_thenOnlyUpdateToolState() {
        // Given
        tool.setState(Tool.ToolState.UNDER_REPAIR);
        when(toolRepository.getById(1L)).thenReturn(tool);
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);

        // When
        toolService.repairToolNoDebt(1L, "11111111-1");

        // Then
        assertThat(tool.isOutOfService()).isFalse();
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.AVAILABLE);
        verify(debtRepository, never()).save(any(Debt.class));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenGetToolsForRepair_thenReturnToolsUnderRepair() {
        // Given
        ToolDTOnoKardex toolDTO = mock(ToolDTOnoKardex.class);
        when(toolRepository.getToolsForRepair()).thenReturn(Arrays.asList(toolDTO));

        // When
        List<ToolDTOnoKardex> result = toolService.getToolsForRepair();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(toolRepository, times(1)).getToolsForRepair();
    }

    @Test
    void whenSetToolToLoaned_thenToolStateIsLoaned() {
        // Given
        when(toolRepository.getById(1L)).thenReturn(tool);

        // When
        toolService.setToolToLoaned(1L);

        // Then
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.LOANED);
    }

    @Test
    void whenUpdateToolByIdAndGroup_thenUpdateAllSimilarTools() {
        // Given
        Tool updatedTool = new Tool();
        updatedTool.setName("Martillo Actualizado");
        updatedTool.setCategory(Tool.ToolCategory.MANUAL);
        updatedTool.setState(Tool.ToolState.AVAILABLE);
        updatedTool.setRentDailyRate(1500);
        updatedTool.setLateFee(750);
        updatedTool.setReplacementValue(6000);
        updatedTool.setRepairCost(1500);
        updatedTool.setOutOfService(false);

        Tool tool2 = new Tool();
        tool2.setId(2L);
        tool2.setName("Martillo");
        tool2.setCategory(Tool.ToolCategory.MANUAL);
        tool2.setRentDailyRate(1000);
        tool2.setLateFee(500);
        tool2.setReplacementValue(5000);

        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(toolRepository.findByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue(
                "Martillo", Tool.ToolCategory.MANUAL, 1000, 500, 5000))
                .thenReturn(Arrays.asList(tool, tool2));
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.saveAll(any())).thenReturn(Arrays.asList(tool, tool2));

        // When
        toolService.updateToolByIdAndGroup(1L, "11111111-1", updatedTool);

        // Then
        verify(toolRepository, times(1)).saveAll(any());
        verify(kardexService, times(2)).saveKardex(any());
    }

    @Test
    void whenUpdateToolByIdAndGroup_toolNotFound_thenThrowException() {
        // Given
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> toolService.updateToolByIdAndGroup(1L, "11111111-1", tool))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tool not found");
    }

    @Test
    void whenGetToolByNameAndCategory_thenReturnTool() {
        // Given
        ToolDTOnoKardex toolDTO = mock(ToolDTOnoKardex.class);
        when(toolRepository.findOneByNameAndCategory("Martillo", Tool.ToolCategory.MANUAL))
                .thenReturn(Arrays.asList(toolDTO));

        // When
        ToolDTOnoKardex result = toolService.getToolByNameAndCategory("Martillo", "MANUAL");

        // Then
        assertThat(result).isNotNull();
        verify(toolRepository, times(1)).findOneByNameAndCategory("Martillo", Tool.ToolCategory.MANUAL);
    }

    @Test
    void whenGetToolByNameAndCategory_notFound_thenReturnNull() {
        // Given
        when(toolRepository.findOneByNameAndCategory("NoExiste", Tool.ToolCategory.MANUAL))
                .thenReturn(Arrays.asList());

        // When
        ToolDTOnoKardex result = toolService.getToolByNameAndCategory("NoExiste", "MANUAL");

        // Then
        assertThat(result).isNull();
    }
}

