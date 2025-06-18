package com.example.Module_de_Facturation.repository;

import com.example.Module_de_Facturation.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour l'accès aux données des factures
 */
public interface FactureRepository extends JpaRepository<Facture, Long> {
    /** Trouve les factures par client */
    List<Facture> findByClientId(Long clientId);
    /** Trouve les factures par date */
    List<Facture> findByDate(LocalDate date);
    /** Trouve les factures par client et date */
    List<Facture> findByClientIdAndDate(Long clientId, LocalDate date);
    /** Trouve les factures entre deux dates */
    List<Facture> findByDateBetween(LocalDate startDate, LocalDate endDate);
}