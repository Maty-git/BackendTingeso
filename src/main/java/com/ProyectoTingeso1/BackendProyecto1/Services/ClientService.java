package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public ArrayList<Client> getClients() {
        return (ArrayList<Client>) clientRepository.findAll();
    }

    public List<String> getAllRuts() {
        return clientRepository.findAllRuts();
    }

    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }


    public boolean deleteClient(Long id) throws Exception {
        try{
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Client getClientByRut(String rut) {
        return clientRepository.findByRut(rut);
    }
}
