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


}