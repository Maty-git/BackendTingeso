package com.ProyectoTingeso1.BackendProyecto1.ControllerTests;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.DebtController;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Services.DebtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DebtController.class)
class DebtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DebtService debtService;

    @Autowired
    private ObjectMapper objectMapper;

    private Debt debt;

    @BeforeEach
    void setUp() {
        debt = new Debt();
        debt.setId(1L);
        debt.setAmount(2500);
        debt.setType("LATE");
        debt.setPaid(false);
    }

    @Test
    void whenCreateDebt_thenReturnCreatedDebt() throws Exception {
        // Given
        when(debtService.saveDebt(any(Debt.class))).thenReturn(debt);

        // When & Then
        mockMvc.perform(post("/api/Debts/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(debt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(2500))
                .andExpect(jsonPath("$.type").value("LATE"));

        verify(debtService, times(1)).saveDebt(any(Debt.class));
    }

    @Test
    void whenGetAllDebt_thenReturnListOfDebts() throws Exception {
        // Given
        List<Debt> debts = Arrays.asList(debt);
        when(debtService.getAllDebt()).thenReturn(debts);

        // When & Then
        mockMvc.perform(get("/api/Debts/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(2500));

        verify(debtService, times(1)).getAllDebt();
    }

    @Test
    void whenGetDebtById_thenReturnDebt() throws Exception {
        // Given
        when(debtService.getDebt(1L)).thenReturn(debt);

        // When & Then
        mockMvc.perform(get("/api/Debts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(2500))
                .andExpect(jsonPath("$.type").value("LATE"));

        verify(debtService, times(1)).getDebt(1L);
    }

    @Test
    void whenPayDebt_thenReturnTrue() throws Exception {
        // Given
        when(debtService.payDebt(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/Debts/pay/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(debtService, times(1)).payDebt(1L);
    }

    @Test
    void whenPayDebtNotFound_thenReturnFalse() throws Exception {
        // Given
        when(debtService.payDebt(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/Debts/pay/999"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(debtService, times(1)).payDebt(999L);
    }

    @Test
    void whenGetUnpaidDebtsSummary_thenReturnSummaries() throws Exception {
        // Given
        when(debtService.getUnpaidDebtsSummary()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/Debts/unpaid"))
                .andExpect(status().isOk());

        verify(debtService, times(1)).getUnpaidDebtsSummary();
    }

    @Test
    void whenGetUnpaidDebtsWhereTypeIsLate_thenReturnClients() throws Exception {
        // Given
        when(debtService.getUnpaidDebtsWhereTypeIsLate()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/Debts/late"))
                .andExpect(status().isOk());

        verify(debtService, times(1)).getUnpaidDebtsWhereTypeIsLate();
    }

    @Test
    void whenGetLateClientsByDateRange_thenReturnClients() throws Exception {
        // Given
        when(debtService.getLateClientsByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/Debts/late/range")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31"))
                .andExpect(status().isOk());

        verify(debtService, times(1)).getLateClientsByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }
}

