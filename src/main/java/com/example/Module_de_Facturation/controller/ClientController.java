package com.example.Module_de_Facturation.controller;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
 private final ClientService clientService;
 public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
 @GetMapping("/{id}")
    public Client getClient(@PathVariable long id){
     return clientService.getClientById(id);
 }
 @PostMapping
    public Client createclient(@RequestBody Client client){
     return clientService.createClient(client);
 }
}
