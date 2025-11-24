package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.ClientService;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import com.ProyectoTingeso1.BackendProyecto1.Services.LoanService;
import com.ProyectoTingeso1.BackendProyecto1.Services.ToolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private ToolService toolService;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private KardexService kardexService;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private LoanService loanService;

    private Client client;
    private Tool tool;
    private Loan loan;
    private LoanRequestDTO loanRequestDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setStatus("ACTIVE");

        tool = new Tool();
        tool.setId(1L);
        tool.setName("Martillo");
        tool.setCategory(Tool.ToolCategory.MANUAL);
        tool.setState(Tool.ToolState.AVAILABLE);
        tool.setQuantity(1);
        tool.setRentDailyRate(1000);
        tool.setLateFee(500);
        tool.setReplacementValue(5000);

        loan = new Loan();
        loan.setId(1L);
        loan.setClient(client);
        loan.setTool(tool);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setQuantity(1);
        loan.setUserRut("11111111-1");

        loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setClientRut("12345678-9");
        loanRequestDTO.setToolId(1L);
        loanRequestDTO.setReturnDateExpected(LocalDateTime.now().plusDays(7));
        loanRequestDTO.setQuantity(1);
        loanRequestDTO.setStatus(Loan.LoanStatus.ACTIVE);
        loanRequestDTO.setUserRut("11111111-1");
    }

    @Test
    void whenSaveLoan_withValidData_thenReturnTrue() {
        // Given
        when(clientService.getClientByRut(anyString())).thenReturn(client);
        when(loanRepository.countActiveLoansByClient(any(Client.class))).thenReturn(0L);
        when(toolService.getToolById(any())).thenReturn(tool);
        when(loanRepository.findActiveLoansByClientAndToolAttributes(any(), anyString(), any()))
                .thenReturn(Collections.emptyList());
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        doNothing().when(toolService).setToolToLoaned(any());

        // When
        boolean result = loanService.saveLoan(loanRequestDTO);

        // Then
        assertThat(result).isTrue();
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(kardexService, times(1)).saveKardex(any());
    }

    @Test
    void whenSaveLoan_withRestrictedClient_thenReturnFalse() {
        // Given
        client.setStatus("RESTRICTED");
        when(clientService.getClientByRut(anyString())).thenReturn(client);

        // When
        boolean result = loanService.saveLoan(loanRequestDTO);

        // Then
        assertThat(result).isFalse();
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenSaveLoan_withFiveActiveLoans_thenReturnFalse() {
        // Given
        when(clientService.getClientByRut(anyString())).thenReturn(client);
        when(loanRepository.countActiveLoansByClient(any(Client.class))).thenReturn(5L);

        // When
        boolean result = loanService.saveLoan(loanRequestDTO);

        // Then
        assertThat(result).isFalse();
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenSaveLoan_withSimilarActiveLoan_thenReturnFalse() {
        // Given
        when(clientService.getClientByRut(anyString())).thenReturn(client);
        when(loanRepository.countActiveLoansByClient(any(Client.class))).thenReturn(0L);
        when(toolService.getToolById(any())).thenReturn(tool);
        when(loanRepository.findActiveLoansByClientAndToolAttributes(any(), anyString(), any()))
                .thenReturn(Arrays.asList(loan));

        // When
        boolean result = loanService.saveLoan(loanRequestDTO);

        // Then
        assertThat(result).isFalse();
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenGetAllLoans_thenReturnLoanList() {
        // Given
        LoanReturnDTO loanDTO = mock(LoanReturnDTO.class);
        when(loanRepository.getLoanSummary()).thenReturn(Arrays.asList(loanDTO));

        // When
        List<LoanReturnDTO> result = loanService.getAllLoans();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).getLoanSummary();
    }

    @Test
    void whenReturnLoan_withoutDamage_thenReturnTrue() {
        // Given
        when(loanRepository.findActiveLoanByIdEntity(any())).thenReturn(loan);
        when(debtRepository.findUnpaidDebtByLoan(any())).thenReturn(null);
        when(debtRepository.findUnpaidDebtsByClient(any())).thenReturn(Collections.emptyList());
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.save(any())).thenReturn(loan);

        // When
        boolean result = loanService.returnLoan(1L, "11111111-1", false);

        // Then
        assertThat(result).isTrue();
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(toolRepository, times(1)).save(any(Tool.class));
        assertThat(loan.getStatus()).isEqualTo(Loan.LoanStatus.RETURNED);
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.AVAILABLE);
    }

    @Test
    void whenReturnLoan_withDamage_thenToolUnderRepair() {
        // Given
        when(loanRepository.findActiveLoanByIdEntity(any())).thenReturn(loan);
        when(debtRepository.findUnpaidDebtByLoan(any())).thenReturn(null);
        when(debtRepository.findUnpaidDebtsByClient(any())).thenReturn(Collections.emptyList());
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.save(any())).thenReturn(loan);

        // When
        boolean result = loanService.returnLoan(1L, "11111111-1", true);

        // Then
        assertThat(result).isTrue();
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.UNDER_REPAIR);
        assertThat(loan.getStatus()).isEqualTo(Loan.LoanStatus.UNPAID_DEBT);
    }

    @Test
    void whenReturnLoan_withUnpaidDebt_thenDebtMarkedAsPaid() {
        // Given
        Debt debt = new Debt();
        debt.setId(1L);
        debt.setPaid(false);

        when(loanRepository.findActiveLoanByIdEntity(any())).thenReturn(loan);
        when(debtRepository.findUnpaidDebtByLoan(any())).thenReturn(debt);
        when(debtRepository.save(any())).thenReturn(debt);
        when(debtRepository.findUnpaidDebtsByClient(any())).thenReturn(Collections.emptyList());
        when(kardexService.saveKardex(any())).thenReturn(ResponseEntity.ok().build());
        when(toolRepository.save(any())).thenReturn(tool);
        when(loanRepository.save(any())).thenReturn(loan);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        boolean result = loanService.returnLoan(1L, "11111111-1", false);

        // Then
        assertThat(result).isTrue();
        verify(debtRepository, times(1)).save(debt);
        assertThat(debt.isPaid()).isTrue();
    }

    @Test
    void whenReturnLoan_loanNotFound_thenReturnFalse() {
        // Given
        when(loanRepository.findActiveLoanByIdEntity(any())).thenReturn(null);

        // When
        boolean result = loanService.returnLoan(999L, "11111111-1", false);

        // Then
        assertThat(result).isFalse();
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenGetLoanReturnDTOById_thenReturnDTO() {
        // Given
        LoanReturnDTO loanDTO = mock(LoanReturnDTO.class);
        when(loanRepository.findActiveLoanById(any())).thenReturn(loanDTO);

        // When
        LoanReturnDTO result = loanService.getLoanReturnDTOById(1L);

        // Then
        assertThat(result).isNotNull();
        verify(loanRepository, times(1)).findActiveLoanById(1L);
    }

    @Test
    void whenGetActiveLoansWithStatus_thenReturnList() {
        // Given
        ActiveLoanStatusDTO statusDTO = mock(ActiveLoanStatusDTO.class);
        when(loanRepository.findAllActiveLoansWithStatus()).thenReturn(Arrays.asList(statusDTO));

        // When
        List<ActiveLoanStatusDTO> result = loanService.getActiveLoansWithStatus();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).findAllActiveLoansWithStatus();
    }

    @Test
    void whenGetActiveLoansWithStatusByDateRange_thenReturnList() {
        // Given
        ActiveLoanStatusDTO statusDTO = mock(ActiveLoanStatusDTO.class);
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(loanRepository.findActiveLoansWithStatusByDateRange(start, end))
                .thenReturn(Arrays.asList(statusDTO));

        // When
        List<ActiveLoanStatusDTO> result = loanService.getActiveLoansWithStatusByDateRange(start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).findActiveLoansWithStatusByDateRange(start, end);
    }

    @Test
    void whenGetMostLoanedToolsByDateRange_thenReturnRanking() {
        // Given
        ToolRankingDTO rankingDTO = mock(ToolRankingDTO.class);
        LocalDateTime start = LocalDateTime.now().minusDays(30);
        LocalDateTime end = LocalDateTime.now();
        when(loanRepository.findMostLoanedToolsByDateRange(start, end))
                .thenReturn(Arrays.asList(rankingDTO));

        // When
        List<ToolRankingDTO> result = loanService.getMostLoanedToolsByDateRange(start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).findMostLoanedToolsByDateRange(start, end);
    }

    @Test
    void whenGetMostLoanedTools_thenReturnRanking() {
        // Given
        ToolRankingDTO rankingDTO = mock(ToolRankingDTO.class);
        when(loanRepository.findMostLoanedTools()).thenReturn(Arrays.asList(rankingDTO));

        // When
        List<ToolRankingDTO> result = loanService.getMostLoanedTools();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).findMostLoanedTools();
    }
}

