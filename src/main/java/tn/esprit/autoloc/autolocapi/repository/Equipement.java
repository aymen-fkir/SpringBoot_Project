package tn.esprit.autoloc.autolocapi.repository;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "equipement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    private String libelle;
    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Vehicule> vehicules;

    @PreRemove
    private void preRemove() {
        if (vehicules != null) {
            for (Vehicule v : vehicules) {
                if (v.getEquipements() != null) {
                    v.getEquipements().remove(this);
                }
            }
        }
    }
}