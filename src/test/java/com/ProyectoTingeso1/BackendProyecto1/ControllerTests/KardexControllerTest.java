package com.ProyectoTingeso1.BackendProyecto1.ControllerTests;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.KardexController;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KardexController.class)
class KardexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KardexService kardexService;

    private Kardex kardex;

    @BeforeEach
    void setUp() {
        kardex = new Kardex();
        kardex.setId(1L);
        kardex.setMovementType(Kardex.MovementType.CREATION);
        kardex.setQuantity(1);
        kardex.setUserRut("11111111-1");
    }

    @Test
    void whenGetAllKardex_thenReturnListOfKardex() throws Exception {
        // Given
        List<Kardex> kardexList = Arrays.asList(kardex);
        when(kardexService.getAllKardex()).thenReturn(kardexList);

        // When & Then
        mockMvc.perform(get("/api/kardex/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].quantity").value(1));

        verify(kardexService, times(1)).getAllKardex();
    }

    @Test
    void whenGetMovementsByTool_thenReturnMovements() throws Exception {
        // Given
        when(kardexService.getMovementsByTool(anyLong())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/kardex/tool/1"))
                .andExpect(status().isOk());

        verify(kardexService, times(1)).getMovementsByTool(1L);
    }

    @Test
    void whenGetMovementsByToolAndDateRange_thenReturnMovementsInRange() throws Exception {
        // Given
        when(kardexService.getMovementsByToolAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList());

        String startDate = LocalDateTime.now().minusDays(7).toString();
        String endDate = LocalDateTime.now().toString();

        // When & Then
        mockMvc.perform(get("/api/kardex/tool/1/range")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk());

        verify(kardexService, times(1)).getMovementsByToolAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}

