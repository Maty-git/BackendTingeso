package com.ProyectoTingeso1.BackendProyecto1.RepositoryTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ActiveLoanStatusDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.LoanReturnDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolRankingDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository loanRepository;

    private Client client;
    private Tool tool;
    private Loan loan1;
    private Loan loan2;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setPhoneNumber("+56912345678");
        client.setEmail("juan@example.com");
        client.setStatus("ACTIVE");

        tool = new Tool();
        tool.setName("Martillo");
        tool.setCategory(Tool.ToolCategory.MANUAL);
        tool.setState(Tool.ToolState.LOANED);
        tool.setQuantity(1);
        tool.setRentDailyRate(1000);
        tool.setLateFee(500);
        tool.setReplacementValue(5000);
        tool.setRepairCost(1000);
        tool.setOutOfService(false);

        loan1 = new Loan();
        loan1.setClient(client);
        loan1.setTool(tool);
        loan1.setReturnDateExpected(LocalDateTime.now().plusDays(7));
        loan1.setQuantity(1);
        loan1.setStatus(Loan.LoanStatus.ACTIVE);
        loan1.setUserRut("11111111-1");
        loan1.setDelay(false);

        loan2 = new Loan();
        loan2.setClient(client);
        loan2.setTool(tool);
        loan2.setReturnDateExpected(LocalDateTime.now().plusDays(14));
        loan2.setQuantity(1);
        loan2.setStatus(Loan.LoanStatus.RETURNED);
        loan2.setUserRut("11111111-1");
        loan2.setDelay(false);

        entityManager.persist(client);
        entityManager.persist(tool);
        entityManager.persist(loan1);
        entityManager.persist(loan2);
        entityManager.flush();
    }

    @Test
    void whenGetLoanSummary_thenReturnActiveLoans() {
        // When
        List<LoanReturnDTO> loans = loanRepository.getLoanSummary();

        // Then
        assertThat(loans).isNotNull();
        assertThat(loans).hasSize(1);
        assertThat(loans.get(0).getClientName()).isEqualTo("Juan Pérez");
    }

    @Test
    void whenFindByStatus_thenReturnLoansWithThatStatus() {
        // When
        List<Loan> activeLoans = loanRepository.findByStatus(Loan.LoanStatus.ACTIVE);
        List<Loan> returnedLoans = loanRepository.findByStatus(Loan.LoanStatus.RETURNED);

        // Then
        assertThat(activeLoans).hasSize(1);
        assertThat(returnedLoans).hasSize(1);
    }

    @Test
    void whenCountActiveLoansByClient_thenReturnCorrectCount() {
        // When
        long count = loanRepository.countActiveLoansByClient(client);

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void whenFindActiveLoansByClientAndToolAttributes_thenReturnMatchingLoans() {
        // When
        List<Loan> loans = loanRepository.findActiveLoansByClientAndToolAttributes(
                client,
                "Martillo",
                Tool.ToolCategory.MANUAL
        );

        // Then
        assertThat(loans).isNotNull();
        assertThat(loans).hasSize(1);
    }

    @Test
    void whenFindActiveLoanById_thenReturnLoanDTO() {
        // When
        LoanReturnDTO loanDTO = loanRepository.findActiveLoanById(loan1.getId());

        // Then
        assertThat(loanDTO).isNotNull();
        assertThat(loanDTO.getClientName()).isEqualTo("Juan Pérez");
        assertThat(loanDTO.getToolName()).isEqualTo("Martillo");
    }

    @Test
    void whenFindActiveLoanByIdEntity_thenReturnLoan() {
        // When
        Loan found = loanRepository.findActiveLoanByIdEntity(loan1.getId());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getStatus()).isEqualTo(Loan.LoanStatus.ACTIVE);
    }

    @Test
    void whenFindLoanByToolAndStatus_thenReturnLoan() {
        // When
        Loan found = loanRepository.findLoanByToolAndStatus(tool, Loan.LoanStatus.ACTIVE);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getTool().getName()).isEqualTo("Martillo");
    }

    @Test
    void whenFindAllActiveLoansWithStatus_thenReturnLoansWithStatus() {
        // When
        List<ActiveLoanStatusDTO> loans = loanRepository.findAllActiveLoansWithStatus();

        // Then
        assertThat(loans).isNotNull();
        assertThat(loans).hasSize(1);
        assertThat(loans.get(0).getClientName()).isEqualTo("Juan Pérez");
    }

    @Test
    void whenFindActiveLoansWithStatusByDateRange_thenReturnLoansInRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // When
        List<ActiveLoanStatusDTO> loans = loanRepository.findActiveLoansWithStatusByDateRange(start, end);

        // Then
        assertThat(loans).isNotNull();
        assertThat(loans).hasSize(1);
    }

    @Test
    void whenFindMostLoanedTools_thenReturnRanking() {
        // When
        List<ToolRankingDTO> ranking = loanRepository.findMostLoanedTools();

        // Then
        assertThat(ranking).isNotNull();
        assertThat(ranking).isNotEmpty();
        assertThat(ranking.get(0).getToolName()).isEqualTo("Martillo");
        assertThat(ranking.get(0).getTotalLoans()).isEqualTo(2);
    }

    @Test
    void whenFindMostLoanedToolsByDateRange_thenReturnRankingInRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // When
        List<ToolRankingDTO> ranking = loanRepository.findMostLoanedToolsByDateRange(start, end);

        // Then
        assertThat(ranking).isNotNull();
        assertThat(ranking).isNotEmpty();
    }
}

