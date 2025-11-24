package com.ProyectoTingeso1.BackendProyecto1.RepositoryTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.DTOs.DebtSummaryDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DebtRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DebtRepository debtRepository;

    private Client client;
    private Tool tool;
    private Loan loan;
    private Debt debt1;
    private Debt debt2;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setPhoneNumber("+56912345678");
        client.setEmail("juan@example.com");
        client.setStatus("RESTRICTED");

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

        loan = new Loan();
        loan.setClient(client);
        loan.setTool(tool);
        loan.setReturnDateExpected(LocalDateTime.now().minusDays(5));
        loan.setQuantity(1);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setUserRut("11111111-1");
        loan.setDelay(true);

        debt1 = new Debt();
        debt1.setLoan(loan);
        debt1.setClient(client);
        debt1.setAmount(2500);
        debt1.setType("LATE");
        debt1.setPaid(false);

        debt2 = new Debt();
        debt2.setLoan(loan);
        debt2.setClient(client);
        debt2.setAmount(1000);
        debt2.setType("DAMAGE");
        debt2.setPaid(true);

        entityManager.persist(client);
        entityManager.persist(tool);
        entityManager.persist(loan);
        entityManager.persist(debt1);
        entityManager.persist(debt2);
        entityManager.flush();
    }

    @Test
    void whenFindByLoan_thenReturnDebt() {
        // When
        List<Debt> debts = debtRepository.findAll();
        
        // Then
        assertThat(debts).isNotEmpty();
        assertThat(debts).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void whenFindDebtById_thenReturnDebt() {
        // When
        Debt found = debtRepository.findDebtById(debt1.getId());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getAmount()).isEqualTo(2500);
        assertThat(found.getType()).isEqualTo("LATE");
    }

    @Test
    void whenFindUnpaidDebtByLoan_thenReturnUnpaidDebt() {
        // When
        Debt found = debtRepository.findUnpaidDebtByLoan(loan);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.isPaid()).isFalse();
        assertThat(found.getType()).isEqualTo("LATE");
    }

    @Test
    void whenFindUnpaidDebtsByClient_thenReturnListOfUnpaidDebts() {
        // When
        List<Debt> unpaidDebts = debtRepository.findUnpaidDebtsByClient(client);

        // Then
        assertThat(unpaidDebts).isNotNull();
        assertThat(unpaidDebts).hasSize(1);
        assertThat(unpaidDebts.get(0).isPaid()).isFalse();
    }

    @Test
    void whenFindAllUnpaidDebtSummaries_thenReturnSummaries() {
        // When
        List<DebtSummaryDTO> summaries = debtRepository.findAllUnpaidDebtSummaries();

        // Then
        assertThat(summaries).isNotNull();
        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).getClientName()).isEqualTo("Juan Pérez");
        assertThat(summaries.get(0).getToolName()).isEqualTo("Martillo");
        assertThat(summaries.get(0).getType()).isEqualTo("LATE");
    }

    @Test
    void whenFindAllUnpaidDebtWhereTypeIsLate_thenReturnLateDebts() {
        // When
        List<DebtSummaryDTO> lateDebts = debtRepository.findAllUnpaidDebtWhereTypeIsLate();

        // Then
        assertThat(lateDebts).isNotNull();
        assertThat(lateDebts).hasSize(1);
        assertThat(lateDebts.get(0).getType()).isEqualTo("LATE");
    }

    @Test
    void whenFindClientsWithLateDebtsInDateRange_thenReturnClients() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // When
        List<ClientDTO> clients = debtRepository.findClientsWithLateDebtsInDateRange(start, end);

        // Then
        assertThat(clients).isNotNull();
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getName()).isEqualTo("Juan Pérez");
    }

    @Test
    void whenMarkDebtAsPaid_thenDebtIsPaid() {
        // Given
        Debt debt = debtRepository.findDebtById(debt1.getId());

        // When
        debt.setPaid(true);
        debtRepository.save(debt);
        entityManager.flush();

        // Then
        Debt updatedDebt = debtRepository.findDebtById(debt1.getId());
        assertThat(updatedDebt.isPaid()).isTrue();
    }

    @Test
    void whenSaveNewDebt_thenDebtIsPersisted() {
        // Given
        Debt newDebt = new Debt();
        newDebt.setLoan(loan);
        newDebt.setClient(client);
        newDebt.setAmount(5000);
        newDebt.setType("REPLACEMENT");
        newDebt.setPaid(false);

        // When
        Debt saved = debtRepository.save(newDebt);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(5000);
    }
}

