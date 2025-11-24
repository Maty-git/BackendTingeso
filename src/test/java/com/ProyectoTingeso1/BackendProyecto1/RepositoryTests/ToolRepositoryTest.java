package com.ProyectoTingeso1.BackendProyecto1.RepositoryTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTOnoKardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ToolRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToolRepository toolRepository;

    private Tool tool1;
    private Tool tool2;
    private Tool tool3;

    @BeforeEach
    void setUp() {
        tool1 = new Tool();
        tool1.setName("Martillo");
        tool1.setCategory(Tool.ToolCategory.MANUAL);
        tool1.setState(Tool.ToolState.AVAILABLE);
        tool1.setQuantity(1);
        tool1.setRentDailyRate(1000);
        tool1.setLateFee(500);
        tool1.setReplacementValue(5000);
        tool1.setRepairCost(1000);
        tool1.setOutOfService(false);

        tool2 = new Tool();
        tool2.setName("Martillo");
        tool2.setCategory(Tool.ToolCategory.MANUAL);
        tool2.setState(Tool.ToolState.LOANED);
        tool2.setQuantity(1);
        tool2.setRentDailyRate(1000);
        tool2.setLateFee(500);
        tool2.setReplacementValue(5000);
        tool2.setRepairCost(1000);
        tool2.setOutOfService(false);

        tool3 = new Tool();
        tool3.setName("Taladro");
        tool3.setCategory(Tool.ToolCategory.ELECTRICAL);
        tool3.setState(Tool.ToolState.UNDER_REPAIR);
        tool3.setQuantity(1);
        tool3.setRentDailyRate(2000);
        tool3.setLateFee(1000);
        tool3.setReplacementValue(20000);
        tool3.setRepairCost(5000);
        tool3.setOutOfService(false);

        entityManager.persist(tool1);
        entityManager.persist(tool2);
        entityManager.persist(tool3);
        entityManager.flush();
    }

    @Test
    void whenGetToolSummary_thenReturnGroupedTools() {
        // When
        List<ToolDTO> summary = toolRepository.getToolSummary();

        // Then
        assertThat(summary).isNotNull();
        assertThat(summary).isNotEmpty();
    }

    @Test
    void whenGetAllToolsNoKardex_thenReturnAllToolsExceptOutOfService() {
        // When
        List<ToolDTOnoKardex> tools = toolRepository.getAllToolsNoKardex();

        // Then
        assertThat(tools).isNotNull();
        assertThat(tools).hasSize(3);
    }

    @Test
    void whenGetToolsForRepair_thenReturnOnlyUnderRepairTools() {
        // When
        List<ToolDTOnoKardex> tools = toolRepository.getToolsForRepair();

        // Then
        assertThat(tools).isNotNull();
        assertThat(tools).hasSize(1);
        assertThat(tools.get(0).getName()).isEqualTo("Taladro");
        assertThat(tools.get(0).getState()).isEqualTo(Tool.ToolState.UNDER_REPAIR);
    }

    @Test
    void whenFindByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue_thenReturnMatchingTools() {
        // When
        List<Tool> tools = toolRepository.findByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue(
                "Martillo",
                Tool.ToolCategory.MANUAL,
                1000,
                500,
                5000
        );

        // Then
        assertThat(tools).isNotNull();
        assertThat(tools).hasSize(2);
    }

    @Test
    void whenFindOneByNameAndCategory_thenReturnTool() {
        // When
        List<ToolDTOnoKardex> tools = toolRepository.findOneByNameAndCategory("Martillo", Tool.ToolCategory.MANUAL);

        // Then
        assertThat(tools).isNotNull();
        assertThat(tools).isNotEmpty();
        assertThat(tools.get(0).getName()).isEqualTo("Martillo");
    }

    @Test
    void whenSaveTool_thenToolIsPersisted() {
        // Given
        Tool newTool = new Tool();
        newTool.setName("Sierra");
        newTool.setCategory(Tool.ToolCategory.CUTTING);
        newTool.setState(Tool.ToolState.AVAILABLE);
        newTool.setQuantity(1);
        newTool.setRentDailyRate(1500);
        newTool.setLateFee(750);
        newTool.setReplacementValue(10000);
        newTool.setRepairCost(2000);
        newTool.setOutOfService(false);

        // When
        Tool saved = toolRepository.save(newTool);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Sierra");
    }

    @Test
    void whenUpdateToolState_thenStateIsUpdated() {
        // Given
        Tool tool = toolRepository.findById(tool1.getId()).orElseThrow();

        // When
        tool.setState(Tool.ToolState.LOANED);
        Tool updated = toolRepository.save(tool);

        // Then
        assertThat(updated.getState()).isEqualTo(Tool.ToolState.LOANED);
    }

    @Test
    void whenMarkToolAsOutOfService_thenToolIsOutOfService() {
        // Given
        Tool tool = toolRepository.findById(tool1.getId()).orElseThrow();

        // When
        tool.setOutOfService(true);
        tool.setState(Tool.ToolState.OUT_OF_SERVICE);
        toolRepository.save(tool);
        entityManager.flush();

        // Then
        List<ToolDTOnoKardex> availableTools = toolRepository.getAllToolsNoKardex();
        assertThat(availableTools).hasSize(2); // No debería incluir la herramienta fuera de servicio
    }
}

