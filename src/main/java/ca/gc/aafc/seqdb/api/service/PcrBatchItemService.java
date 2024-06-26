package ca.gc.aafc.seqdb.api.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import ca.gc.aafc.dina.jpa.PredicateSupplier;
import ca.gc.aafc.dina.translator.NumberLetterTranslator;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import lombok.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

@Service
public class PcrBatchItemService extends DefaultDinaService<PcrBatchItem> {

  public PcrBatchItemService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(PcrBatchItem entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull PredicateSupplier<T> where,
                             BiFunction<CriteriaBuilder, Root<T>, List<Order>> orderBy,
                             int startIndex, int maxResult, @NonNull Set<String> includes,
                             @NonNull Set<String> relationships)  {

    List<T> itemsList = super.findAll(entityClass, where, orderBy, startIndex, maxResult, includes, relationships);
    if (PcrBatchItem.class == entityClass) {
     // itemsList.forEach(t -> setCellNumber((PcrBatchItem) t));
    }
    return itemsList;
  }

//  private void setCellNumber(PcrBatchItem item) {
//    PcrBatch batch = item.getPcrBatch();
//    if (batch == null || batch.getStorageRestriction() == null) {
//      return;
//    }
//
//    // We assume row and col are valid, so we only check for row presence
//    if (StringUtils.isBlank(item.getWellRow())) {
//      return;
//    }
//
//    StorageGridLayout restriction = batch.getStorageRestriction().getLayout();
//    item.setCellNumber(restriction.calculateCellNumber(NumberLetterTranslator.toNumber(item.getWellRow()), item.getWellColumn()));
//  }

}
