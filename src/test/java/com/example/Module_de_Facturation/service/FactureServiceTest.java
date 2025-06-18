package com.example.Module_de_Facturation.service;

import com.example.Module_de_Facturation.model.Client;
import com.example.Module_de_Facturation.model.Facture;
import com.example.Module_de_Facturation.model.LigneFacture;
import com.example.Module_de_Facturation.repository.ClientRepository;
import com.example.Module_de_Facturation.repository.FactureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service
 */
@ExtendWith(MockitoExtension.class)
class FactureServiceTest {

    // Mock des dépendances
    @Mock
    private FactureRepository factureRepository;
    @Mock
    private ClientRepository clientRepository;

    // Service à tester avec les mocks injectés
    @InjectMocks
    private FactureService factureService;

    // Données de test
    private Client testClient;
    private LigneFacture validLigne;

    /**
     * Initialisation avant chaque test
     */
    @BeforeEach
    void setUp() {
        // Création d'un client de test
        testClient = new Client();
        testClient.setId(1L);
        testClient.setNom("Client Test");

        // Création d'une ligne de facture valide
        validLigne = new LigneFacture();
        validLigne.setDescription("Service conseil");
        validLigne.setQuantite(2);
        validLigne.setPrixUnitaireHT(100.0);
        validLigne.setTauxTVA(20.0);
    }

    /**
     * Test la création valide d'une facture
     */
    @Test
    void createFacture_WithValidData_ShouldCalculateTotals() {
        // Configuration du mock
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(factureRepository.save(any(Facture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode à tester
        Facture result = factureService.createFacture(
                1L,
                LocalDate.now(),
                Collections.singletonList(validLigne)
        );

        // Vérifications
        assertNotNull(result);
        assertEquals(200.0, result.getTotalHT()); // 2 x 100 = 200 HT
        assertEquals(40.0, result.getTotalTVA());  // 200 x 20% = 40 TVA
        assertEquals(240.0, result.getTotalTTC()); // 200 + 40 = 240 TTC
        assertEquals(1, result.getLignes().size()); // 1 ligne
    }

    /**
     * Test la création d'une facture sans lignes
     */
    @Test
    void createFacture_WithoutLines_ShouldThrowException() {
        // Test et vérification de l'exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factureService.createFacture(1L, LocalDate.now(), Collections.emptyList())
        );

        assertEquals("Une facture doit avoir au moins une ligne", exception.getMessage());
    }

    /**
     * Test la création avec un taux de TVA invalide
     */
    @Test
    void createFacture_WithInvalidTVA_ShouldThrowException() {
        // Création d'une ligne avec TVA invalide
        LigneFacture invalidLigne = new LigneFacture();
        invalidLigne.setDescription("Service");
        invalidLigne.setQuantite(1);
        invalidLigne.setPrixUnitaireHT(100.0);
        invalidLigne.setTauxTVA(15.0); // Taux invalide

        // Configuration du mock
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        // Test et vérification
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factureService.createFacture(1L, LocalDate.now(), Collections.singletonList(invalidLigne))
        );

        assertEquals("Taux de TVA invalide. Doit être 0%, 5.5%, 10% ou 20%", exception.getMessage());
    }

    /**
     * Test la création avec une ligne incomplète
     */
    @Test
    void createFacture_WithIncompleteLine_ShouldThrowException() {
        // Création d'une ligne incomplète (description manquante)
        LigneFacture incompleteLigne = new LigneFacture();
        incompleteLigne.setQuantite(1);
        incompleteLigne.setPrixUnitaireHT(100.0);
        incompleteLigne.setTauxTVA(20.0);

        // Configuration du mock
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        // Test et vérification
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factureService.createFacture(1L, LocalDate.now(), Collections.singletonList(incompleteLigne))
        );

        assertEquals("Tous les champs de la ligne doivent être remplis", exception.getMessage());
    }
}