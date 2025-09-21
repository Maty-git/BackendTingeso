package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DebtService {
    @Autowired
    private DebtRepository debtRepository;

    public Debt saveDebt(Debt debt) {
        return debtRepository.save(debt);
    }

}
