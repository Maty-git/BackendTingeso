package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kardex")
@CrossOrigin("*")
public class KardexController {
    @Autowired
    KardexService kardexService;

    @GetMapping("/all")
    public ResponseEntity<List<Kardex>> getAllKardex() {
        List<Kardex> listKardex = kardexService.getAllKardex();
        return ResponseEntity.ok().body(listKardex);
    }


}
