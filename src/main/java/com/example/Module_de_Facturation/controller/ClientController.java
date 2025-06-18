package com.example.Module_de_Facturation.controller;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.service.ClientService;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des clients
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {
 private final ClientService clientService;
    // Injection du service via le constructeur
 public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    /**
     * Récupère un client par son ID
     */
 @GetMapping("/{id}")
    public Client getClient(@PathVariable long id){
     return clientService.getClientById(id);
 }
    /**
     * Crée un nouveau client
     */
 @PostMapping
    public Client createclient(@RequestBody Client client){
     return clientService.createClient(client);
 }
}
