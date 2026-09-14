package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.dina.entity.Address;
import ca.gc.aafc.dina.entity.DinaEntity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@NaturalIdCache
@Table(name = "sequencing_facility")
public class SequencingFacility implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @NotNull
  @Column(unique = true)
  private UUID uuid;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Size(max = 50)
  @Column(name = "_group")
  private String group;

  @NotBlank
  @Size(max = 50)
  private String name;

  @Type(JsonType.class)
  @Column(name = "contacts", columnDefinition = "jsonb")
  @Valid
  private List<ContactRole> contacts = List.of();

  @Type(JsonType.class)
  @Column(name = "shipping_address", columnDefinition = "jsonb")
  @Valid
  private Address shippingAddress;

  @Data
  @Builder
  public static class ContactRole implements Serializable {

    @NotEmpty
    private String name;

    @NotEmpty
    private List<@NotBlank String> roles;

    @Size(max = 100)
    private String info;

  }
}
