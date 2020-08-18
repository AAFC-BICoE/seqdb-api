package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne ;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "PcrProfiles",
  uniqueConstraints = { @UniqueConstraint(columnNames = "Name") }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class PcrProfile {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Size(max = 50)
  private String name;

  private String application;
  private String cycles;
  private String step1;
  private String step2;
  private String step3;
  private String step4;
  private String step5;
  private String step6;
  private String step7;
  private String step8;
  private String step9;
  private String step10;
  private String step11;
  private String step12;
  private String step13;
  private String step14;
  private String step15;

  @Version
  private Timestamp lastModified;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn
  }))
  private Region region;

}