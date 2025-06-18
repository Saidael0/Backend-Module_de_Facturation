package com.example.Module_de_Facturation.controller;

import com.example.Module_de_Facturation.DTO.FactureRequest;
import com.example.Module_de_Facturation.model.Facture;
import com.example.Module_de_Facturation.service.FactureService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Contrôleur pour la gestion des factures
 */
@RestController
@RequestMapping("/api/factures")
public class FactureController {
    public final FactureService factureService;
    public FactureController(FactureService factureService){
        this.factureService = factureService;
    }
    /**
     * Récupère toutes les factures
     */
    @GetMapping
    public List<Facture> getAllFactures(){
        return factureService.getAllFactures();
    }
    /**
     * Récupère une facture par son ID
     */
    @GetMapping("/{id}")
    public Facture getFacture(@PathVariable long id){
        return  factureService.getFactureById(id);
    }
    /**
     * Crée une nouvelle facture
     */
    @PostMapping
    public Facture createFacture(@RequestBody FactureRequest request) {
        return factureService.createFacture(
                request.getClientId(),
                request.getDate(),
                request.getLignes()
        );
    }
    /**
     * Récupère les factures d'un client
     */
    @GetMapping("/client/{clientId}")
    public List<Facture> getFacturesByClient(@PathVariable Long clientId) {
        return factureService.getFacturesByClient(clientId);
    }
    /**
     * Récupère les factures par date
     */
    @GetMapping("/date/{date}")
    public List<Facture> getFacturesByDate(@PathVariable String date) {
        return factureService.getFacturesByDate(LocalDate.parse(date));
    }
    /**
     * Exporte une facture au format JSON
     */
    @GetMapping("/{id}/export")
    public Facture exportFacture(@PathVariable Long id) {
        return factureService.getFactureById(id);
    }
    /**
     * Recherche avancée de factures
     */
    @GetMapping("/search")
    public List<Facture> searchFactures(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        // Logique de recherche combinée
        if (clientId != null && date != null) {
            return factureService.findByClientAndDate(clientId, LocalDate.parse(date));
        } else if (clientId != null) {
            return factureService.getFacturesByClient(clientId);
        } else if (date != null) {
            return factureService.getFacturesByDate(LocalDate.parse(date));
        } else if (startDate != null && endDate != null) {
            return factureService.findByDateBetween(startDate, endDate);
        } else {
            return factureService.getAllFactures();
        }
    }
}
