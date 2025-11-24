package com.ProyectoTingeso1.BackendProyecto1.EntityTests;

import com.ProyectoTingeso1.BackendProyecto1.Entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    @Test
    void testClientEntity() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setPhoneNumber("+56912345678");
        client.setEmail("juan@example.com");
        client.setStatus("ACTIVE");
        client.setLoans(new ArrayList<>());
        client.setDebts(new ArrayList<>());

        // Then
        assertThat(client.getId()).isEqualTo(1L);
        assertThat(client.getName()).isEqualTo("Juan Pérez");
        assertThat(client.getRut()).isEqualTo("12345678-9");
        assertThat(client.getPhoneNumber()).isEqualTo("+56912345678");
        assertThat(client.getEmail()).isEqualTo("juan@example.com");
        assertThat(client.getStatus()).isEqualTo("ACTIVE");
        assertThat(client.getLoans()).isEmpty();
        assertThat(client.getDebts()).isEmpty();
    }

    @Test
    void testClientEqualsAndHashCode() {
        // Given
        Client client1 = new Client(1L, "Juan Pérez", "12345678-9", "+56912345678", 
                                     "juan@example.com", "ACTIVE", null, null);
        Client client2 = new Client(1L, "Juan Pérez", "12345678-9", "+56912345678", 
                                     "juan@example.com", "ACTIVE", null, null);
        Client client3 = new Client(2L, "María González", "98765432-1", "+56987654321", 
                                     "maria@example.com", "RESTRICTED", null, null);

        // Then
        assertThat(client1).isEqualTo(client2);
        assertThat(client1).isNotEqualTo(client3);
        assertThat(client1.hashCode()).isEqualTo(client2.hashCode());
    }

    @Test
    void testToolEntity() {
        // Given
        Tool tool = new Tool();
        tool.setId(1L);
        tool.setName("Martillo");
        tool.setCategory(Tool.ToolCategory.MANUAL);
        tool.setState(Tool.ToolState.AVAILABLE);
        tool.setQuantity(1);
        tool.setRentDailyRate(1000);
        tool.setLateFee(500);
        tool.setReplacementValue(5000);
        tool.setRepairCost(1000);
        tool.setOutOfService(false);

        // Then
        assertThat(tool.getId()).isEqualTo(1L);
        assertThat(tool.getName()).isEqualTo("Martillo");
        assertThat(tool.getCategory()).isEqualTo(Tool.ToolCategory.MANUAL);
        assertThat(tool.getState()).isEqualTo(Tool.ToolState.AVAILABLE);
        assertThat(tool.getQuantity()).isEqualTo(1);
        assertThat(tool.getRentDailyRate()).isEqualTo(1000);
        assertThat(tool.getLateFee()).isEqualTo(500);
        assertThat(tool.getReplacementValue()).isEqualTo(5000);
        assertThat(tool.getRepairCost()).isEqualTo(1000);
        assertThat(tool.isOutOfService()).isFalse();
    }

    @Test
    void testToolEnums() {
        // Test ToolState
        assertThat(Tool.ToolState.valueOf("AVAILABLE")).isEqualTo(Tool.ToolState.AVAILABLE);
        assertThat(Tool.ToolState.valueOf("LOANED")).isEqualTo(Tool.ToolState.LOANED);
        assertThat(Tool.ToolState.valueOf("UNDER_REPAIR")).isEqualTo(Tool.ToolState.UNDER_REPAIR);
        assertThat(Tool.ToolState.valueOf("OUT_OF_SERVICE")).isEqualTo(Tool.ToolState.OUT_OF_SERVICE);

        // Test ToolCategory
        assertThat(Tool.ToolCategory.valueOf("MANUAL")).isEqualTo(Tool.ToolCategory.MANUAL);
        assertThat(Tool.ToolCategory.valueOf("ELECTRICAL")).isEqualTo(Tool.ToolCategory.ELECTRICAL);
        assertThat(Tool.ToolCategory.valueOf("CONSTRUCTION")).isEqualTo(Tool.ToolCategory.CONSTRUCTION);
    }

    @Test
    void testLoanEntity() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturnDateExpected(now.plusDays(7));
        loan.setRealReturnDate(null);
        loan.setQuantity(1);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setUserRut("11111111-1");
        loan.setDelay(false);

        // Then
        assertThat(loan.getId()).isEqualTo(1L);
        assertThat(loan.getReturnDateExpected()).isEqualTo(now.plusDays(7));
        assertThat(loan.getRealReturnDate()).isNull();
        assertThat(loan.getQuantity()).isEqualTo(1);
        assertThat(loan.getStatus()).isEqualTo(Loan.LoanStatus.ACTIVE);
        assertThat(loan.getUserRut()).isEqualTo("11111111-1");
        assertThat(loan.getDelay()).isFalse();
    }

    @Test
    void testLoanStatusEnum() {
        assertThat(Loan.LoanStatus.valueOf("ACTIVE")).isEqualTo(Loan.LoanStatus.ACTIVE);
        assertThat(Loan.LoanStatus.valueOf("RETURNED")).isEqualTo(Loan.LoanStatus.RETURNED);
        assertThat(Loan.LoanStatus.valueOf("UNPAID_DEBT")).isEqualTo(Loan.LoanStatus.UNPAID_DEBT);
        assertThat(Loan.LoanStatus.valueOf("TOOL_BROKE")).isEqualTo(Loan.LoanStatus.TOOL_BROKE);
    }

    @Test
    void testDebtEntity() {
        // Given
        Debt debt = new Debt();
        debt.setId(1L);
        debt.setAmount(2500);
        debt.setType("LATE");
        debt.setPaid(false);

        // Then
        assertThat(debt.getId()).isEqualTo(1L);
        assertThat(debt.getAmount()).isEqualTo(2500);
        assertThat(debt.getType()).isEqualTo("LATE");
        assertThat(debt.isPaid()).isFalse();
    }

    @Test
    void testKardexEntity() {
        // Given
        Kardex kardex = new Kardex();
        kardex.setId(1L);
        kardex.setMovementType(Kardex.MovementType.CREATION);
        kardex.setQuantity(1);
        kardex.setUserRut("11111111-1");

        // Then
        assertThat(kardex.getId()).isEqualTo(1L);
        assertThat(kardex.getMovementType()).isEqualTo(Kardex.MovementType.CREATION);
        assertThat(kardex.getQuantity()).isEqualTo(1);
        assertThat(kardex.getUserRut()).isEqualTo("11111111-1");
    }

    @Test
    void testKardexMovementTypeEnum() {
        assertThat(Kardex.MovementType.valueOf("CREATION")).isEqualTo(Kardex.MovementType.CREATION);
        assertThat(Kardex.MovementType.valueOf("LOAN")).isEqualTo(Kardex.MovementType.LOAN);
        assertThat(Kardex.MovementType.valueOf("RETURN")).isEqualTo(Kardex.MovementType.RETURN);
        assertThat(Kardex.MovementType.valueOf("CANCELLATION")).isEqualTo(Kardex.MovementType.CANCELLATION);
        assertThat(Kardex.MovementType.valueOf("REPAIR")).isEqualTo(Kardex.MovementType.REPAIR);
        assertThat(Kardex.MovementType.valueOf("UPDATE")).isEqualTo(Kardex.MovementType.UPDATE);
    }

    @Test
    void testLoanWithAllArgsConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Client client = new Client();
        Tool tool = new Tool();
        
        Loan loan = new Loan(1L, now.plusDays(7), now.plusDays(7), 1, 
                            Loan.LoanStatus.RETURNED, now, "11111111-1", false, 
                            client, tool, new ArrayList<>());

        // Then
        assertThat(loan.getId()).isEqualTo(1L);
        assertThat(loan.getStatus()).isEqualTo(Loan.LoanStatus.RETURNED);
        assertThat(loan.getClient()).isEqualTo(client);
        assertThat(loan.getTool()).isEqualTo(tool);
    }

    @Test
    void testToolWithAllArgsConstructor() {
        // Given
        Tool tool = new Tool(1L, "Martillo", Tool.ToolCategory.MANUAL, 
                            Tool.ToolState.AVAILABLE, 1, 1000, 500, 5000, 1000, 
                            false, new ArrayList<>(), new ArrayList<>());

        // Then
        assertThat(tool.getId()).isEqualTo(1L);
        assertThat(tool.getName()).isEqualTo("Martillo");
        assertThat(tool.getCategory()).isEqualTo(Tool.ToolCategory.MANUAL);
    }

    @Test
    void testToStringMethods() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setName("Test");

        Tool tool = new Tool();
        tool.setId(1L);
        tool.setName("Martillo");

        Debt debt = new Debt();
        debt.setId(1L);
        debt.setAmount(100);

        Kardex kardex = new Kardex();
        kardex.setId(1L);

        Loan loan = new Loan();
        loan.setId(1L);

        // Then - Just verify toString doesn't throw exception
        assertThat(client.toString()).isNotNull();
        assertThat(tool.toString()).isNotNull();
        assertThat(debt.toString()).isNotNull();
        assertThat(kardex.toString()).isNotNull();
        assertThat(loan.toString()).isNotNull();
    }
}

