package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import lombok.NonNull;

@Service
public class PreLibraryPrepService extends DinaService<PreLibraryPrep> {

  public PreLibraryPrepService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(PreLibraryPrep entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(PreLibraryPrep entity) {

  }

  @Override
  protected void preUpdate(PreLibraryPrep entity) {

  }
  
}
