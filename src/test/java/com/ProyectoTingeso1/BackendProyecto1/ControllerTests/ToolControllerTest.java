package com.ProyectoTingeso1.BackendProyecto1.ControllerTests;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.ToolController;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Services.ToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToolController.class)
class ToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolService toolService;

    @Autowired
    private ObjectMapper objectMapper;

    private Tool tool;

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
    }

    @Test
    void whenSaveTool_thenReturnTrue() throws Exception {
        // Given
        when(toolService.saveTool(any(Tool.class), anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/tools/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tool)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(toolService, times(1)).saveTool(any(Tool.class), eq("11111111-1"));
    }

    @Test
    void whenGetAllTools_thenReturnListOfTools() throws Exception {
        // Given
        when(toolService.getAllTools()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/tools/all"))
                .andExpect(status().isOk());

        verify(toolService, times(1)).getAllTools();
    }

    @Test
    void whenGetTools_thenReturnListOfToolsNoKardex() throws Exception {
        // Given
        when(toolService.getAllToolsNoKardex()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/tools/tools"))
                .andExpect(status().isOk());

        verify(toolService, times(1)).getAllToolsNoKardex();
    }

    @Test
    void whenDeleteTool_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(toolService).deleteToolById(1L, "11111111-1");

        // When & Then
        mockMvc.perform(put("/api/tools/1/11111111-1"))
                .andExpect(status().isNoContent());

        verify(toolService, times(1)).deleteToolById(1L, "11111111-1");
    }

    @Test
    void whenRepairTool_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(toolService).repairTool(1L, "11111111-1");

        // When & Then
        mockMvc.perform(put("/api/tools/repairDebt/1/11111111-1"))
                .andExpect(status().isNoContent());

        verify(toolService, times(1)).repairTool(1L, "11111111-1");
    }

    @Test
    void whenRepairToolNoDebt_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(toolService).repairToolNoDebt(1L, "11111111-1");

        // When & Then
        mockMvc.perform(put("/api/tools/repair/1/11111111-1"))
                .andExpect(status().isNoContent());

        verify(toolService, times(1)).repairToolNoDebt(1L, "11111111-1");
    }

    @Test
    void whenGetToolsForRepair_thenReturnList() throws Exception {
        // Given
        when(toolService.getToolsForRepair()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/tools/for-repair"))
                .andExpect(status().isOk());

        verify(toolService, times(1)).getToolsForRepair();
    }

    @Test
    void whenUpdateTool_thenReturnNoContent() throws Exception {
        // Given
        doNothing().when(toolService).updateToolByIdAndGroup(any(), anyString(), any(Tool.class));

        // When & Then
        mockMvc.perform(put("/api/tools/update/1/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tool)))
                .andExpect(status().isNoContent());

        verify(toolService, times(1)).updateToolByIdAndGroup(eq(1L), eq("11111111-1"), any(Tool.class));
    }

    @Test
    void whenGetToolByNameAndCategory_thenReturnTool() throws Exception {
        // Given
        when(toolService.getToolByNameAndCategory("Martillo", "MANUAL")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/tools/Martillo/MANUAL"))
                .andExpect(status().isOk());

        verify(toolService, times(1)).getToolByNameAndCategory("Martillo", "MANUAL");
    }
}

