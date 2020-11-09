package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.Product;
import lombok.NonNull;

@Service
public class ProductService extends DinaService<Product> {

  public ProductService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Product entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(Product entity) {

  }

  @Override
  protected void preUpdate(Product entity) {

  }
  
}
