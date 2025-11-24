package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.DebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtServiceTest {

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DebtService debtService;

    private Debt debt;
    private Loan loan;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setStatus("RESTRICTED");

        loan = new Loan();
        loan.setId(1L);
        loan.setClient(client);
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        debt = new Debt();
        debt.setId(1L);
        debt.setLoan(loan);
        debt.setClient(client);
        debt.setAmount(2500);
        debt.setType("LATE");
        debt.setPaid(false);
    }

    @Test
    void whenSaveDebt_thenDebtIsSaved() {
        // Given
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        // When
        Debt saved = debtService.saveDebt(debt);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(2500);
        verify(debtRepository, times(1)).save(debt);
    }

    @Test
    void whenGetAllDebt_thenReturnAllDebts() {
        // Given
        List<Debt> debts = Arrays.asList(debt);
        when(debtRepository.findAll()).thenReturn(debts);

        // When
        List<Debt> result = debtService.getAllDebt();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(debtRepository, times(1)).findAll();
    }

    @Test
    void whenGetDebt_thenReturnDebt() {
        // Given
        when(loanRepository.findActiveLoanByIdEntity(anyLong())).thenReturn(loan);
        when(debtRepository.findUnpaidDebtByLoan(any(Loan.class))).thenReturn(debt);

        // When
        Debt found = debtService.getDebt(1L);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getType()).isEqualTo("LATE");
        verify(loanRepository, times(1)).findActiveLoanByIdEntity(1L);
        verify(debtRepository, times(1)).findUnpaidDebtByLoan(loan);
    }

    @Test
    void whenPayDebt_andNoUnpaidDebtsRemain_thenClientStatusBecomesActive() {
        // Given
        when(debtRepository.findDebtById(anyLong())).thenReturn(debt);
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);
        when(debtRepository.findUnpaidDebtsByClient(any(Client.class))).thenReturn(Collections.emptyList());
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        boolean result = debtService.payDebt(1L);

        // Then
        assertThat(result).isTrue();
        verify(debtRepository, times(1)).findDebtById(1L);
        verify(debtRepository, times(1)).save(debt);
        verify(clientRepository, times(1)).save(client);
        assertThat(client.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void whenPayDebt_andUnpaidDebtsRemain_thenClientStatusStaysRestricted() {
        // Given
        Debt otherDebt = new Debt();
        otherDebt.setId(2L);
        otherDebt.setPaid(false);

        when(debtRepository.findDebtById(anyLong())).thenReturn(debt);
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);
        when(debtRepository.findUnpaidDebtsByClient(any(Client.class))).thenReturn(Arrays.asList(otherDebt));

        // When
        boolean result = debtService.payDebt(1L);

        // Then
        assertThat(result).isTrue();
        verify(debtRepository, times(1)).findDebtById(1L);
        verify(debtRepository, times(1)).save(debt);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenPayDebtNotFound_thenReturnFalse() {
        // Given
        when(debtRepository.findDebtById(anyLong())).thenReturn(null);

        // When
        boolean result = debtService.payDebt(999L);

        // Then
        assertThat(result).isFalse();
        verify(debtRepository, times(1)).findDebtById(999L);
        verify(debtRepository, never()).save(any(Debt.class));
    }

    @Test
    void whenGetUnpaidDebtsSummary_thenReturnSummaries() {
        // Given
        DebtSummaryDTO summary = mock(DebtSummaryDTO.class);
        when(debtRepository.findAllUnpaidDebtSummaries()).thenReturn(Arrays.asList(summary));

        // When
        List<DebtSummaryDTO> result = debtService.getUnpaidDebtsSummary();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(debtRepository, times(1)).findAllUnpaidDebtSummaries();
    }

    @Test
    void whenGetUnpaidDebtsWhereTypeIsLate_thenReturnClients() {
        // Given
        DebtSummaryDTO debtSummary = mock(DebtSummaryDTO.class);
        when(debtSummary.getClientRut()).thenReturn("12345678-9");

        when(debtRepository.findAllUnpaidDebtWhereTypeIsLate()).thenReturn(Arrays.asList(debtSummary));
        when(clientRepository.findClientDTOByRut("12345678-9")).thenReturn(null);

        // When
        List<ClientDTO> result = debtService.getUnpaidDebtsWhereTypeIsLate();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
        verify(debtRepository, times(1)).findAllUnpaidDebtWhereTypeIsLate();
    }

    @Test
    void whenGetUnpaidDebtsWhereTypeIsLate_withNullClient_thenSkipIt() {
        // Given
        DebtSummaryDTO debtSummary1 = mock(DebtSummaryDTO.class);
        when(debtSummary1.getClientRut()).thenReturn("12345678-9");

        when(debtRepository.findAllUnpaidDebtWhereTypeIsLate()).thenReturn(Arrays.asList(debtSummary1));
        when(clientRepository.findClientDTOByRut("12345678-9")).thenReturn(null);

        // When
        List<ClientDTO> result = debtService.getUnpaidDebtsWhereTypeIsLate();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(debtRepository, times(1)).findAllUnpaidDebtWhereTypeIsLate();
    }

    @Test
    void whenGetLateClientsByDateRange_thenReturnClients() {
        // Given
        ClientDTO clientDTO = mock(ClientDTO.class);
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        
        when(debtRepository.findClientsWithLateDebtsInDateRange(start, end))
                .thenReturn(Arrays.asList(clientDTO));

        // When
        List<ClientDTO> result = debtService.getLateClientsByDateRange(start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(debtRepository, times(1)).findClientsWithLateDebtsInDateRange(start, end);
    }
}

