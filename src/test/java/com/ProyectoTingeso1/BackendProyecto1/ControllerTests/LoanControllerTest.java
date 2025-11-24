package com.ProyectoTingeso1.BackendProyecto1.ControllerTests;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.LoanController;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoanRequestDTO loanRequestDTO;

    @BeforeEach
    void setUp() {
        loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setClientRut("12345678-9");
        loanRequestDTO.setToolId(1L);
        loanRequestDTO.setReturnDateExpected(LocalDateTime.now().plusDays(7));
        loanRequestDTO.setQuantity(1);
        loanRequestDTO.setStatus(Loan.LoanStatus.ACTIVE);
        loanRequestDTO.setUserRut("11111111-1");
    }

    @Test
    void whenSaveLoan_thenReturnTrue() throws Exception {
        // Given
        when(loanService.saveLoan(any(LoanRequestDTO.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/loans/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(loanService, times(1)).saveLoan(any(LoanRequestDTO.class));
    }

    @Test
    void whenSaveLoan_withRestrictedClient_thenReturnFalse() throws Exception {
        // Given
        when(loanService.saveLoan(any(LoanRequestDTO.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/loans/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(loanService, times(1)).saveLoan(any(LoanRequestDTO.class));
    }

    @Test
    void whenGetAllLoan_thenReturnListOfLoans() throws Exception {
        // Given
        when(loanService.getAllLoans()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/loans/all"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getAllLoans();
    }

    @Test
    void whenGetLoanById_thenReturnLoan() throws Exception {
        // Given
        when(loanService.getLoanReturnDTOById(1L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getLoanReturnDTOById(1L);
    }

    @Test
    void whenReturnLoan_thenReturnTrue() throws Exception {
        // Given
        when(loanService.returnLoan(1L, "11111111-1", false)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/loans/return/1/11111111-1/false"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(loanService, times(1)).returnLoan(1L, "11111111-1", false);
    }

    @Test
    void whenReturnLoan_withDamage_thenReturnTrue() throws Exception {
        // Given
        when(loanService.returnLoan(1L, "11111111-1", true)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/loans/return/1/11111111-1/true"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(loanService, times(1)).returnLoan(1L, "11111111-1", true);
    }

    @Test
    void whenGetActiveLoansWithStatus_thenReturnList() throws Exception {
        // Given
        when(loanService.getActiveLoansWithStatus()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/loans/active"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getActiveLoansWithStatus();
    }

    @Test
    void whenGetActiveLoansWithStatusByDateRange_thenReturnList() throws Exception {
        // Given
        when(loanService.getActiveLoansWithStatusByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/loans/active/range")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getActiveLoansWithStatusByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void whenGetMostLoanedToolsByDateRange_thenReturnRanking() throws Exception {
        // Given
        when(loanService.getMostLoanedToolsByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/loans/ranking/range")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-31"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getMostLoanedToolsByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void whenGetMostLoanedTools_thenReturnRanking() throws Exception {
        // Given
        when(loanService.getMostLoanedTools()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/loans/ranking"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).getMostLoanedTools();
    }
}

