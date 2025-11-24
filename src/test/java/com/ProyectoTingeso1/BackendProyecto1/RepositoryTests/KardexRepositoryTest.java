package com.ProyectoTingeso1.BackendProyecto1.RepositoryTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.KardexDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.KardexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class KardexRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KardexRepository kardexRepository;

    private Tool tool;
    private Kardex kardex1;
    private Kardex kardex2;
    private Kardex kardex3;

    @BeforeEach
    void setUp() {
        tool = new Tool();
        tool.setName("Martillo");
        tool.setCategory(Tool.ToolCategory.MANUAL);
        tool.setState(Tool.ToolState.AVAILABLE);
        tool.setQuantity(1);
        tool.setRentDailyRate(1000);
        tool.setLateFee(500);
        tool.setReplacementValue(5000);
        tool.setRepairCost(1000);
        tool.setOutOfService(false);

        kardex1 = new Kardex();
        kardex1.setMovementType(Kardex.MovementType.CREATION);
        kardex1.setQuantity(1);
        kardex1.setUserRut("11111111-1");
        kardex1.setTool(tool);

        kardex2 = new Kardex();
        kardex2.setMovementType(Kardex.MovementType.LOAN);
        kardex2.setQuantity(1);
        kardex2.setUserRut("22222222-2");
        kardex2.setTool(tool);

        kardex3 = new Kardex();
        kardex3.setMovementType(Kardex.MovementType.RETURN);
        kardex3.setQuantity(1);
        kardex3.setUserRut("22222222-2");
        kardex3.setTool(tool);

        entityManager.persist(tool);
        entityManager.persist(kardex1);
        entityManager.persist(kardex2);
        entityManager.persist(kardex3);
        entityManager.flush();
    }

    @Test
    void whenFindMovementsByToolId_thenReturnAllMovementsForTool() {
        // When
        List<KardexDTO> movements = kardexRepository.findMovementsByToolId(tool.getId());

        // Then
        assertThat(movements).isNotNull();
        assertThat(movements).hasSize(3);
        assertThat(movements.get(0).getToolName()).isEqualTo("Martillo");
    }

    @Test
    void whenFindMovementsByToolAndDateRange_thenReturnMovementsInRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        // When
        List<KardexDTO> movements = kardexRepository.findMovementsByToolAndDateRange(tool.getId(), start, end);

        // Then
        assertThat(movements).isNotNull();
        assertThat(movements).hasSize(3);
    }

    @Test
    void whenFindMovementsByToolAndDateRange_withNoMovementsInRange_thenReturnEmpty() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now().minusDays(9);

        // When
        List<KardexDTO> movements = kardexRepository.findMovementsByToolAndDateRange(tool.getId(), start, end);

        // Then
        assertThat(movements).isEmpty();
    }

    @Test
    void whenSaveKardexMovement_thenMovementIsPersisted() {
        // Given
        Kardex newMovement = new Kardex();
        newMovement.setMovementType(Kardex.MovementType.UPDATE);
        newMovement.setQuantity(1);
        newMovement.setUserRut("33333333-3");
        newMovement.setTool(tool);

        // When
        Kardex saved = kardexRepository.save(newMovement);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMovementType()).isEqualTo(Kardex.MovementType.UPDATE);
    }

    @Test
    void whenFindAll_thenReturnAllKardexMovements() {
        // When
        List<Kardex> allMovements = kardexRepository.findAll();

        // Then
        assertThat(allMovements).isNotNull();
        assertThat(allMovements).hasSize(3);
    }
}

