package com.example.Module_de_Facturation.DTO;

import com.example.Module_de_Facturation.model.LigneFacture;

import java.time.LocalDate;
import java.util.List;

public class FactureRequest {
    private Long clientId;
    private LocalDate date;
    private List<LigneFacture> lignes;

    // Getters et setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<LigneFacture> getLignes() { return lignes; }
    public void setLignes(List<LigneFacture> lignes) { this.lignes = lignes; }
}