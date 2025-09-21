package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByRut(String rut);
    @Query("SELECT c.rut FROM Client c")
    List<String> findAllRuts();
}
