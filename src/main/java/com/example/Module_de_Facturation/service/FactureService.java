package com.example.Module_de_Facturation.service;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.model.Facture;
import com.example.Module_de_Facturation.model.LigneFacture;
import com.example.Module_de_Facturation.repository.ClientRepository;
import com.example.Module_de_Facturation.repository.FactureRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FactureService {

        private final FactureRepository factureRepository;
        private final ClientRepository clientRepository;

        public FactureService(FactureRepository factureRepository, ClientRepository clientRepository) {
            this.factureRepository = factureRepository;
            this.clientRepository = clientRepository;
        }

        @Transactional
        public Facture createFacture(Long clientId, LocalDate date, List<LigneFacture> lignes) {
            if (lignes == null || lignes.isEmpty()) {
                throw new IllegalArgumentException("Une facture doit avoir au moins une ligne");
            }

            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

            Facture facture = new Facture();
            facture.setClient(client);
            facture.setDate(date);

            // Calcul des totaux
            double totalHT = 0;
            double totalTVA = 0;

            for (LigneFacture ligne : lignes) {
                if (ligne.getDescription() == null || ligne.getDescription().isEmpty() ||
                        ligne.getQuantite() <= 0 || ligne.getPrixUnitaireHT() <= 0) {
                    throw new IllegalArgumentException("Tous les champs de la ligne doivent être remplis");
                }

                // Vérification du taux de TVA valide
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

            facture.setTotalHT(totalHT);
            facture.setTotalTVA(totalTVA);
            facture.setTotalTTC(totalHT + totalTVA);
            facture.setLignes(lignes);

            return factureRepository.save(facture);
        }

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
    }