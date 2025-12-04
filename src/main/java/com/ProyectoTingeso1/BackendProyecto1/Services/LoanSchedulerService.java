package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanSchedulerService {

    private final LoanRepository loanRepository;
    private final DebtRepository debtRepository;
    private final ClientRepository clientRepository;

    public LoanSchedulerService(LoanRepository loanRepository, DebtRepository debtRepository,
            ClientRepository clientRepository) {
        this.loanRepository = loanRepository;
        this.debtRepository = debtRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Ejecuta todos los días a las 12:00 del día
     */
    @Scheduled(cron = "0 43 1 * * *", zone = "America/Santiago")
    @Transactional
    public void actualizarPrestamosAtrasados() {
        System.out.println("--- EJECUTANDO SCHEDULER DE PRESTAMOS: " + java.time.LocalDateTime.now() + " ---");
        List<Loan> activs = loanRepository.findByStatus(Loan.LoanStatus.ACTIVE);
        LocalDate today = LocalDate.now();

        for (Loan loan : activs) {
            LocalDate agreedDate = loan.getReturnDateExpected().toLocalDate();

            // Verificamos solo AÑO-MES-DÍA
            if (agreedDate.isBefore(today)) {
                loan.setDelay(true);

                // Calcular multa por días de atraso
                long delayDays = ChronoUnit.DAYS.between(agreedDate, today);
                int multa = (int) (delayDays * loan.getTool().getLateFee());

                // Buscar si ya existe deuda asociada al préstamo
                Debt existDebt = debtRepository.findByLoan(loan);

                if (existDebt != null && !existDebt.isPaid()) {
                    // Si ya había deuda sin pagar, actualizar monto
                    existDebt.setAmount(multa);
                    debtRepository.save(existDebt);
                } else if (existDebt == null) {
                    // Si no había deuda, crear una nueva
                    Debt newDebt = new Debt();
                    newDebt.setLoan(loan);
                    newDebt.setClient(loan.getClient());
                    newDebt.setAmount(multa);
                    newDebt.setType("LATE");
                    newDebt.setPaid(false);

                    debtRepository.save(newDebt);
                }

                // Restringir cliente
                Client client = loan.getClient();
                if (!"RESTRICTED".equals(client.getStatus())) {
                    client.setStatus("RESTRICTED");
                    clientRepository.save(client);
                }

                loanRepository.save(loan);
            }
        }
    }
}
