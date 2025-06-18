package com.example.Module_de_Facturation.service;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client createClient(Client client) {
        client.setDateCreation(LocalDate.now());
        return clientRepository.save(client);
    }
}