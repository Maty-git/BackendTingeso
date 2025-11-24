package com.ProyectoTingeso1.BackendProyecto1.DTOTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanRequestDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DTOTest {

    @Test
    void testLoanRequestDTO() {
        // Given
        LoanRequestDTO dto = new LoanRequestDTO();
        LocalDateTime expectedDate = LocalDateTime.now().plusDays(7);
        
        // When
        dto.setClientRut("12345678-9");
        dto.setUserRut("11111111-1");
        dto.setToolId(1L);
        dto.setQuantity(1);
        dto.setReturnDateExpected(expectedDate);
        dto.setStatus(Loan.LoanStatus.ACTIVE);

        // Then
        assertThat(dto.getClientRut()).isEqualTo("12345678-9");
        assertThat(dto.getUserRut()).isEqualTo("11111111-1");
        assertThat(dto.getToolId()).isEqualTo(1L);
        assertThat(dto.getQuantity()).isEqualTo(1);
        assertThat(dto.getReturnDateExpected()).isEqualTo(expectedDate);
        assertThat(dto.getStatus()).isEqualTo(Loan.LoanStatus.ACTIVE);
    }

    @Test
    void testLoanRequestDTOToString() {
        // Given
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setClientRut("12345678-9");
        dto.setToolId(1L);

        // Then
        assertThat(dto.toString()).isNotNull();
        assertThat(dto.toString()).contains("12345678-9");
    }

    @Test
    void testLoanRequestDTOEqualsAndHashCode() {
        // Given
        LoanRequestDTO dto1 = new LoanRequestDTO();
        dto1.setClientRut("12345678-9");
        dto1.setToolId(1L);

        LoanRequestDTO dto2 = new LoanRequestDTO();
        dto2.setClientRut("12345678-9");
        dto2.setToolId(1L);

        LoanRequestDTO dto3 = new LoanRequestDTO();
        dto3.setClientRut("98765432-1");
        dto3.setToolId(2L);

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void testLoanRequestDTOAllFields() {
        // Given
        LocalDateTime expectedDate = LocalDateTime.now().plusDays(7);
        
        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setClientRut("12345678-9");
        dto.setUserRut("11111111-1");
        dto.setToolId(5L);
        dto.setQuantity(2);
        dto.setReturnDateExpected(expectedDate);
        dto.setStatus(Loan.LoanStatus.RETURNED);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getClientRut()).isEqualTo("12345678-9");
        assertThat(dto.getUserRut()).isEqualTo("11111111-1");
        assertThat(dto.getToolId()).isEqualTo(5L);
        assertThat(dto.getQuantity()).isEqualTo(2);
        assertThat(dto.getReturnDateExpected()).isEqualTo(expectedDate);
        assertThat(dto.getStatus()).isEqualTo(Loan.LoanStatus.RETURNED);
    }
}

