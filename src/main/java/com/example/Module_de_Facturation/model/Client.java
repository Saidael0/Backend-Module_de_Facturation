package com.example.Module_de_Facturation.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String siret;
    private LocalDate dateCreation;
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Facture> factures;
}