package com.example.Module_de_Facturation.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class LigneFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int quantite;
    private double prixUnitaireHT;
    private double tauxTVA; // 0, 5.5, 10, 20
    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonBackReference
    private Facture facture;
}
