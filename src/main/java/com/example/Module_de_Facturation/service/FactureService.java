package com.example.Module_de_Facturation.service;

import com.example.Module_de_Facturation.model.*;
import com.example.Module_de_Facturation.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des factures
 */
@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;

    public FactureService(FactureRepository factureRepository,
                          ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Crée une nouvelle facture avec validation des règles métier
     */
    @Transactional
    public Facture createFacture(Long clientId, LocalDate date, List<LigneFacture> lignes) {

        // Validation : au moins une ligne

        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("Une facture doit avoir au moins une ligne");
        }

        // Vérifie que le client existe
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        Facture facture = new Facture();
        facture.setClient(client);
        facture.setDate(date);

        // Calcul des totaux
        double totalHT = 0;
        double totalTVA = 0;

        for (LigneFacture ligne : lignes) {
            // Validation: champs obligatoires
            if (ligne.getDescription() == null || ligne.getDescription().isEmpty() ||
                    ligne.getQuantite() <= 0 || ligne.getPrixUnitaireHT() <= 0) {
                throw new IllegalArgumentException("Tous les champs de la ligne doivent être remplis");
            }

            // Validation : taux TVA valide
            double taux = ligne.getTauxTVA();
            if (taux != 0 && taux != 5.5 && taux != 10 && taux != 20) {
                throw new IllegalArgumentException("Taux de TVA invalide. Doit être 0%, 5.5%, 10% ou 20%");
            }

            ligne.setFacture(facture);
            double montantHT = ligne.getQuantite() * ligne.getPrixUnitaireHT();
            double montantTVA = montantHT * (taux / 100);

            totalHT += montantHT;
            totalTVA += montantTVA;
        }

        // Enregistrement des totaux
        facture.setTotalHT(totalHT);
        facture.setTotalTVA(totalTVA);
        facture.setTotalTTC(totalHT + totalTVA);
        facture.setLignes(lignes);

        return factureRepository.save(facture);
    }

    // Méthodes de recherche...
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Facture getFactureById(Long id) {
        return factureRepository.findById(id).orElse(null);
    }

    public List<Facture> getFacturesByClient(Long clientId) {
        return factureRepository.findByClientId(clientId);
    }

    public List<Facture> getFacturesByDate(LocalDate date) {
        return factureRepository.findByDate(date);
    }

    public List<Facture> findByClientAndDate(Long clientId, LocalDate date) {
        return factureRepository.findByClientIdAndDate(clientId, date);
    }

    public List<Facture> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return factureRepository.findByDateBetween(startDate, endDate);
    }
}