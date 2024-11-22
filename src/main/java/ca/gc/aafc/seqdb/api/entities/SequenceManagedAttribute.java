package ca.gc.aafc.seqdb.api.entities;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;

import ca.gc.aafc.dina.entity.DinaEntityIdentifiableByName;
import ca.gc.aafc.dina.entity.ManagedAttribute;
import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.i18n.MultilingualTitle;

@Entity(name = "managed_attribute")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NaturalIdCache
public class SequenceManagedAttribute implements ManagedAttribute, DinaEntityIdentifiableByName {

  public enum ManagedAttributeComponent {
    GENERIC_MOLECULAR_ANALYSIS;

    public static ManagedAttributeComponent fromString(String s) {
      for (ManagedAttributeComponent source : ManagedAttributeComponent.values()) {
        if (source.name().equalsIgnoreCase(s)) {
          return source;
        }
      }
      return null;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @NotNull
  @Column(name = "uuid", unique = true)
  private UUID uuid;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @NotBlank
  private String name;

  @Type(type = "jsonb")
  @Column(name = "multilingual_description")
  @Valid
  private MultilingualDescription multilingualDescription;

  @NotBlank
  @Column(name = "_group")
  @Size(max = 50)
  private String group;

  @NotBlank
  @Size(max = 50)
  @Column(updatable = false)
  private String key;

  @NotNull
  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private VocabularyElementType vocabularyElementType;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "component")
  private ManagedAttributeComponent managedAttributeComponent;

  @Type(type = "string-array")
  @Column(name ="accepted_values", columnDefinition = "text[]")
  private String[] acceptedValues;

  @Size(max = 50)
  private String unit;

  @Override
  public String getTerm() {
    return null;
  }

  @Override
  public MultilingualTitle getMultilingualTitle() {
    return null;
  }

}