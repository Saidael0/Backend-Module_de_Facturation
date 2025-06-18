package com.example.Module_de_Facturation.service;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des clients
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /** Récupère tous les clients */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /** Récupère un client par son ID */
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    /** Crée un nouveau client */
    public Client createClient(Client client) {
        client.setDateCreation(LocalDate.now());
        return clientRepository.save(client);
    }
}