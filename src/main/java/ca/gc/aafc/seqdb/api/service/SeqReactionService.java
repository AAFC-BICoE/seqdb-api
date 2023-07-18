package ca.gc.aafc.seqdb.api.service;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.jpa.PredicateSupplier;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.dina.translator.NumberLetterTranslator;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import ca.gc.aafc.seqdb.api.validation.SeqReactionValidator;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class SeqReactionService extends DefaultDinaService<SeqReaction> {

  private final SeqReactionValidator validator;

  public SeqReactionService(
    @NonNull BaseDAO baseDAO,
    SeqReactionValidator validator,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
    this.validator = validator;
  }

  @Override
  public <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull PredicateSupplier<T> where,
                             BiFunction<CriteriaBuilder, Root<T>, List<Order>> orderBy,
                             int startIndex, int maxResult, @NonNull Set<String> includes,
                             @NonNull Set<String> relationships)  {

    List<T> itemsList = super.findAll(entityClass, where, orderBy, startIndex, maxResult, includes, relationships);
    if(SeqReaction.class == entityClass) {
      itemsList.forEach(t -> setCellNumber((SeqReaction) t));
    }
    return itemsList;
  }

  private void setCellNumber(SeqReaction item) {

    SeqBatch batch = item.getSeqBatch();
    if (batch == null || batch.getStorageRestriction() == null) {
      return;
    }

    // We assume row and col are valid, so we only check for row presence
    if (StringUtils.isBlank(item.getWellRow())) {
      return;
    }

    StorageGridLayout restriction = batch.getStorageRestriction().getLayout();
    item.setCellNumber(restriction.calculateCellNumber(NumberLetterTranslator.toNumber(item.getWellRow()), item.getWellColumn()));
  }

  @Override
  protected void preCreate(SeqReaction entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(SeqReaction entity) {
    applyBusinessRule(entity, validator);
  }

}
