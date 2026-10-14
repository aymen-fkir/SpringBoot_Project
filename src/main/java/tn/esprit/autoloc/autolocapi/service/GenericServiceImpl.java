package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
abstract public class GenericServiceImpl implements IGenericService{
    protected final IAgenceRepository agenceRepository;
    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("id can't be null");
        }
        if (!check(agence, true)) {
            throw new IllegalStateException("Agence non valide");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(long id) {
        return agenceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("agnce" , Long.toString(id)));
    }

    @Override
    public List<Agence> findAll() {
        return List.of();
    }

    @Override
    public Agence update(Long id,Agence agence) {
        if (!this.check(agence, false)) {
            return null;
        }
        try {
            this.findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("agnce" , Long.toString(id));
        }
        return agenceRepository.save(agence);
    }

    @Override
    public void deleteByid(Long id) {
        this.findById(id);
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }

    abstract String getEntityName();
    abstract String getId();
    abstract Void setFields(Long id, Agence agence);
}
