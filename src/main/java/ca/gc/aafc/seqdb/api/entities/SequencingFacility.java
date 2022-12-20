package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

  @Type(type = "jsonb")
  @Column(name = "contacts", columnDefinition = "jsonb")
  @Valid
  private List<ContactRole> contacts = List.of();

  @Type(type = "jsonb")
  @Column(name = "shipping_address", columnDefinition = "jsonb")
  @Valid
  private Address shippingAddress;

  @Data
  @Builder
  public static class ContactRole {

    @NotEmpty
    private String name;

    @NotEmpty
    private List<@NotBlank String> roles;

    @Size(max = 100)
    private String info;

  }

  // TODO to be replaced by dina-base-api version in 0.99
  @Data
  @Builder
  public static class Address {

    @Size (max = 150)
    private String addressLine1;

    @Size (max = 150)
    private String addressLine2;

    @Size (max = 150)
    private String city;

    @Size (max = 150)
    private String provinceState;

    @Size (max = 50)
    private String zipCode;

    @Size (max = 50)
    private String country;

  }
}
