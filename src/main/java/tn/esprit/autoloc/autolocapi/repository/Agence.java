package tn.esprit.autoloc.autolocapi.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties(ignoreUnknown = false)
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getIdAgence() {
        return this.id;
    }

    public void setIdAgence(Long idAgence) {
        this.id = idAgence;
    }

    private String nom;
    private String ville;
    private String adresse;
    private String telephone;

    // 11. Champ mappé entier nommé effectif
    @Column(name = "effectif")
    private Integer effectif;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Vehicule> vehicules;
    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Employe> employes;

    // 10. Méthodes au niveau des entités pour symétriser la relation agence/employé
    public void addEmploye(Employe employe) {
        if (this.employes == null) {
            this.employes = new java.util.HashSet<>();
        }
        this.employes.add(employe);
        employe.setAgence(this);
    }

    public void removeEmploye(Employe employe) {
        if (this.employes != null) {
            this.employes.remove(employe);
        }
        employe.setAgence(null);
    }
}