package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.QualityControl;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = QualityControlDto.TYPENAME)
@TypeName(QualityControlDto.TYPENAME)
@RelatedEntity(QualityControl.class)
public class QualityControlDto {

  public static final String TYPENAME = "quality-control";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;

  private String name;

  private String qcType;

  @JsonApiRelation
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

}
