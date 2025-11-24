package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.LoanSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanSchedulerServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private LoanSchedulerService loanSchedulerService;

    private Loan loan;
    private Client client;
    private Tool tool;
    private Debt existingDebt;

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
        tool.setLateFee(500);

        loan = new Loan();
        loan.setId(1L);
        loan.setClient(client);
        loan.setTool(tool);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setReturnDateExpected(LocalDateTime.now().minusDays(5));
        loan.setDelay(false);

        existingDebt = new Debt();
        existingDebt.setId(1L);
        existingDebt.setLoan(loan);
        existingDebt.setClient(client);
        existingDebt.setAmount(1500);
        existingDebt.setType("LATE");
        existingDebt.setPaid(false);
    }

    @Test
    void whenActualizarPrestamosAtrasados_withOverdueLoans_thenCreateDebtsAndRestrictClients() {
        // Given
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Arrays.asList(loan));
        when(debtRepository.findByLoan(loan)).thenReturn(null);
        when(debtRepository.save(any(Debt.class))).thenReturn(existingDebt);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        assertThat(loan.getDelay()).isTrue();
        assertThat(client.getStatus()).isEqualTo("RESTRICTED");
        verify(debtRepository, times(1)).save(any(Debt.class));
        verify(clientRepository, times(1)).save(client);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void whenActualizarPrestamosAtrasados_withExistingUnpaidDebt_thenUpdateDebtAmount() {
        // Given
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Arrays.asList(loan));
        when(debtRepository.findByLoan(loan)).thenReturn(existingDebt);
        when(debtRepository.save(any(Debt.class))).thenReturn(existingDebt);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        verify(debtRepository, times(1)).save(existingDebt);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void whenActualizarPrestamosAtrasados_withPaidDebt_thenNoProcessing() {
        // Given
        existingDebt.setPaid(true);
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Arrays.asList(loan));
        when(debtRepository.findByLoan(loan)).thenReturn(existingDebt);

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        // Como la deuda está pagada, no se hace nada adicional
        verify(loanRepository, times(1)).findByStatus(Loan.LoanStatus.ACTIVE);
        verify(debtRepository, times(1)).findByLoan(loan);
    }

    @Test
    void whenActualizarPrestamosAtrasados_withNoOverdueLoans_thenNoChanges() {
        // Given
        loan.setReturnDateExpected(LocalDateTime.now().plusDays(5));
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Arrays.asList(loan));

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        verify(debtRepository, never()).save(any(Debt.class));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void whenActualizarPrestamosAtrasados_withNoActiveLoans_thenNoProcessing() {
        // Given
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Collections.emptyList());

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        verify(debtRepository, never()).save(any(Debt.class));
        verify(clientRepository, never()).save(any(Client.class));
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void whenActualizarPrestamosAtrasados_clientAlreadyRestricted_thenDontUpdateStatus() {
        // Given
        client.setStatus("RESTRICTED");
        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(Arrays.asList(loan));
        when(debtRepository.findByLoan(loan)).thenReturn(null);
        when(debtRepository.save(any(Debt.class))).thenReturn(existingDebt);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // When
        loanSchedulerService.actualizarPrestamosAtrasados();

        // Then
        verify(clientRepository, never()).save(any(Client.class));
    }
}

