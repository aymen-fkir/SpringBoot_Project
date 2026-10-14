package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;

import java.util.List;
@Service
public class AgenceServiceImpl extends GenericServiceImpl{

    public AgenceServiceImpl(IAgenceRepository agenceRepository) {
        super(agenceRepository);
    }
    public String getEntityName(){
        return "Agence";
    };

    public String getId() {
        return null;
    }

    public Void setFields(Long id, Agence agence) {
        return null;
    }
}
