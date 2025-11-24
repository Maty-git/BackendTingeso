package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.KardexRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;

    @InjectMocks
    private KardexService kardexService;

    private Kardex kardex;
    private KardexDTO kardexDTO;

    @BeforeEach
    void setUp() {
        kardex = new Kardex();
        kardex.setId(1L);
        kardex.setMovementType(Kardex.MovementType.CREATION);
        kardex.setQuantity(1);
        kardex.setUserRut("11111111-1");

        kardexDTO = mock(KardexDTO.class);
    }

    @Test
    void whenSaveKardex_thenReturnResponseEntityWithKardex() {
        // Given
        when(kardexRepository.save(any(Kardex.class))).thenReturn(kardex);

        // When
        ResponseEntity<Object> response = kardexService.saveKardex(kardex);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(kardex);
        verify(kardexRepository, times(1)).save(kardex);
    }

    @Test
    void whenGetAllKardex_thenReturnAllKardexMovements() {
        // Given
        List<Kardex> kardexList = Arrays.asList(kardex);
        when(kardexRepository.findAll()).thenReturn(kardexList);

        // When
        List<Kardex> result = kardexService.getAllKardex();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(kardexRepository, times(1)).findAll();
    }

    @Test
    void whenGetMovementsByTool_thenReturnMovementsForTool() {
        // Given
        Long toolId = 1L;
        when(kardexRepository.findMovementsByToolId(toolId)).thenReturn(Arrays.asList(kardexDTO));

        // When
        List<KardexDTO> result = kardexService.getMovementsByTool(toolId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(kardexRepository, times(1)).findMovementsByToolId(toolId);
    }

    @Test
    void whenGetMovementsByToolAndDateRange_thenReturnMovementsInRange() {
        // Given
        Long toolId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(kardexRepository.findMovementsByToolAndDateRange(toolId, start, end))
                .thenReturn(Arrays.asList(kardexDTO));

        // When
        List<KardexDTO> result = kardexService.getMovementsByToolAndDateRange(toolId, start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(kardexRepository, times(1)).findMovementsByToolAndDateRange(toolId, start, end);
    }

    @Test
    void whenGetMovementsByToolAndDateRange_withNoResults_thenReturnEmptyList() {
        // Given
        Long toolId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(kardexRepository.findMovementsByToolAndDateRange(toolId, start, end))
                .thenReturn(Arrays.asList());

        // When
        List<KardexDTO> result = kardexService.getMovementsByToolAndDateRange(toolId, start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(kardexRepository, times(1)).findMovementsByToolAndDateRange(toolId, start, end);
    }
}

