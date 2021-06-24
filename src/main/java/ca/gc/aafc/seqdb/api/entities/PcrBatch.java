package ca.gc.aafc.seqdb.api.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "PcrBatches"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class PcrBatch implements DinaEntity {

  @Getter(onMethod = @__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    }))
  private Integer id;

  @Getter(onMethod = @__({
    @NotNull,
    @NaturalId
    }))
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Getter(onMethod = @__({
    @Column(name = "groupname")
    }))
  private String group;

  private UUID[] experimenters;

  @Getter(onMethod = @__({
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY),
    @JoinColumn(name = "PrimerForwardID")    
    }))
  private PcrPrimer primerForward;

  @Getter(onMethod = @__({
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY),
    @JoinColumn(name = "PrimerReverseID")
    }))
  private PcrPrimer primerReverse;

  @Getter(onMethod = @__({
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY),
    @JoinColumn(name = "RegionID")
    }))
  private Region region;
  
}
