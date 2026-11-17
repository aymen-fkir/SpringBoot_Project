package tn.esprit.autoloc.autolocapi.service.interfaces;

public interface IGenericService <T,ID>
        extends IReadServices<T,ID>, IBusiness<T,ID>,IWriteService<T,ID>{
}
