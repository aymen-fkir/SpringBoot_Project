package tn.esprit.autoloc.autolocapi.repository;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.autoloc.autolocapi.repository.Vehicule;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
    @OneToMany(mappedBy = "agence", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Vehicule> vehicules;
    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    private Set<Employe> employes;


}