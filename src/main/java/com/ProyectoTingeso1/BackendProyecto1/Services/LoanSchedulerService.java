package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Loan;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanSchedulerService {

    private final LoanRepository loanRepository;
    private final DebtRepository debtRepository;

    public LoanSchedulerService(LoanRepository loanRepository, DebtRepository debtRepository) {
        this.loanRepository = loanRepository;
        this.debtRepository = debtRepository;
    }

    /**
     * Ejecuta todos los días a las 12:00 del día
     */
    @Scheduled(cron = "0 40 15 * * *")
    @Transactional
    public void actualizarPrestamosAtrasados() {
        List<Loan> activos = loanRepository.findByStatus(Loan.LoanStatus.ACTIVE);

        LocalDate hoy = LocalDate.now();

        for (Loan loan : activos) {
            LocalDate fechaPactada = loan.getReturnDateExpected().toLocalDate();

            // Verificamos solo AÑO-MES-DÍA
            if (fechaPactada.isBefore(hoy)) {
                loan.setDelay(true);
                //loan.setStatus(Loan.LoanStatus.UNPAID_DEBT); no deveria ya que aun no se a devuelto la herramienta

                // Calcular multa por días de atraso
                long diasAtraso = ChronoUnit.DAYS.between(fechaPactada, hoy);
                int multa = (int) (diasAtraso * loan.getTool().getLateFee());

                Debt deuda = new Debt();
                deuda.setLoan(loan);
                deuda.setClient(loan.getClient());
                deuda.setAmount(multa);
                deuda.setType("LATE");
                deuda.setPaid(false);

                debtRepository.save(deuda);
                loanRepository.save(loan);

                //System.out.println("🔔 Loan " + loan.getId() + " marcado como atrasado. Multa: " + multa);
            }
        }
    }
}
