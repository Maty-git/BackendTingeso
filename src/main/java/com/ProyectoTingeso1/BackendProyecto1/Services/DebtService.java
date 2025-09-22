package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Debt;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {
    @Autowired
    private DebtRepository debtRepository;

    public Debt saveDebt(Debt debt) {
        return debtRepository.save(debt);
    }
    public List<Debt> getAllDebt() {return debtRepository.findAll();}
}
