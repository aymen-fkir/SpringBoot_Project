package tn.esprit.autoloc.autolocapi.service;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import java.util.Optional;

@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence,Long>{


    public AgenceServiceImpl(IAgenceRepository agenceRepository) {
        super(agenceRepository);
    }
    public boolean check(Agence entity, boolean isNew) {
        return Utils.isNotEmpty(entity.getNom()) && Utils.isNotEmpty(entity.getTelephone());
    }
    public String getEntityName(){
        return "Agence";
    };

    public Long getId(Agence entity) {
        return entity.getIdAgence();
    }

    public void  setFields(Long id, Agence agence) {
        Optional<Agence> agence1 = this.repository.findById((id));
        if (agence1.isPresent()) {
            this.repository.save(agence);
        }
    }
}
