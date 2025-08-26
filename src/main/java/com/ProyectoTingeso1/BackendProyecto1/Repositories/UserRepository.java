package com.ProyectoTingeso1.BackendProyecto1.Repositories;

import com.ProyectoTingeso1.BackendProyecto1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
