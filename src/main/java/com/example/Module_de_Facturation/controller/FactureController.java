package com.example.Module_de_Facturation.controller;

import com.example.Module_de_Facturation.DTO.FactureRequest;
import com.example.Module_de_Facturation.model.Facture;
import com.example.Module_de_Facturation.service.FactureService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {
    public final FactureService factureService;
    public FactureController(FactureService factureService){
        this.factureService = factureService;
    }
    @GetMapping
    public List<Facture> getAllFactures(){
        return factureService.getAllFactures();
    }
    @GetMapping("/{id}")
    public Facture getFacture(@PathVariable long id){
        return  factureService.getFactureById(id);
    }
    @PostMapping
    public Facture createFacture(@RequestBody FactureRequest request) {
        return factureService.createFacture(
                request.getClientId(),
                request.getDate(),
                request.getLignes()
        );
    }

    @GetMapping("/client/{clientId}")
    public List<Facture> getFacturesByClient(@PathVariable Long clientId) {
        return factureService.getFacturesByClient(clientId);
    }

    @GetMapping("/date/{date}")
    public List<Facture> getFacturesByDate(@PathVariable String date) {
        return factureService.getFacturesByDate(LocalDate.parse(date));
    }

    @GetMapping("/{id}/export")
    public Facture exportFacture(@PathVariable Long id) {
        return factureService.getFactureById(id);
    }
}
