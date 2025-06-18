package com.example.Module_de_Facturation.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Entity
@Data
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private double totalHT;
    private double totalTVA;
    private double totalTTC;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LigneFacture> lignes;
}