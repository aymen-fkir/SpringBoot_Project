package tn.esprit.autoloc.autolocapi.repository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import tn.esprit.autoloc.autolocapi.domain.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmploye;

    private String nom;
    private String prenom;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private RoleEmploye role;
    @ManyToOne
    @JoinColumn(name="agence_id_agence")
    @JsonBackReference
    private Agence agence;

    // 10. Méthodes au niveau des entités pour symétriser la relation agence/employé
    public void affecterAgence(Agence agence) {
        this.agence = agence;
        if (agence != null) {
            if (agence.getEmployes() == null) {
                agence.setEmployes(new java.util.HashSet<>());
            }
            agence.getEmployes().add(this);
        }
    }

    public void desaffecterAgence() {
        if (this.agence != null && this.agence.getEmployes() != null) {
            this.agence.getEmployes().remove(this);
        }
        this.agence = null;
    }
}