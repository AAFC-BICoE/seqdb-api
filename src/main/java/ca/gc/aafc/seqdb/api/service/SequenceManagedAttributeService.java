package ca.gc.aafc.seqdb.api.service;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.NonNull;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.ManagedAttributeService;
import ca.gc.aafc.dina.service.PostgresJsonbService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

@Service
public class SequenceManagedAttributeService extends ManagedAttributeService<SequenceManagedAttribute> {

  public static final String MANAGED_ATTRIBUTES_COL_NAME = "managed_attributes";

  public static final String GENERIC_MOLECULAR_ANALYSIS_TABLE_NAME = "generic_molecular_analysis";

  private static final String COMPONENT_FIELD_NAME = "managedAttributeComponent";

  private final PostgresJsonbService jsonbService;

  public SequenceManagedAttributeService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    @NonNull PostgresJsonbService postgresJsonbService) {
    super(baseDAO, sv, SequenceManagedAttribute.class);
    this.jsonbService = postgresJsonbService;
  }

  @Override
  protected void preCreate(SequenceManagedAttribute entity) {
    entity.setUuid(UUIDHelper.generateUUIDv7());
    entity.setGroup(standardizeGroupName(entity));
    super.preCreate(entity);
  }

  @Override
  protected void preDelete(SequenceManagedAttribute entity) {
    switch (entity.getManagedAttributeComponent()) {
      case GENERIC_MOLECULAR_ANALYSIS:
        checkKeysFor(entity.getKey(), GENERIC_MOLECULAR_ANALYSIS_TABLE_NAME);
        break;
      default:
        throw new IllegalStateException(
          "Unexpected managed attribute component of: " + entity.getManagedAttributeComponent());
    }
  }

  public SequenceManagedAttribute findOneByKeyAndComponent(String key,
                                                           SequenceManagedAttribute.ManagedAttributeComponent component) {
    if (StringUtils.isBlank(key) || component == null) {
      return null;
    }
    return findOneByKeyAnd(key, Pair.of(COMPONENT_FIELD_NAME, component));
  }

  private void checkKeysFor(String key, String tableName) {
    Integer countFirstLevelKeys = jsonbService.countFirstLevelKeys(
      tableName, SequenceManagedAttributeService.MANAGED_ATTRIBUTES_COL_NAME, key);
    if (countFirstLevelKeys > 0) {
      throw new IllegalStateException("Managed attribute key: " + key + ", is currently in use.");
    }
  }
}
