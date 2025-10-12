package com.ProyectoTingeso1.BackendProyecto1.Controllers;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client clientNew = clientService.saveClient(client);
        return ResponseEntity.ok(clientNew);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Client>>  listClients() {
        List<Client> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }
    @GetMapping("/rutsClients")
    public ResponseEntity<List<String>> listClientsRut() {
        List<String> ruts = clientService.getAllRuts();
        return ResponseEntity.ok(ruts);
    }
    @PutMapping("/update")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable Long id) throws Exception {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
