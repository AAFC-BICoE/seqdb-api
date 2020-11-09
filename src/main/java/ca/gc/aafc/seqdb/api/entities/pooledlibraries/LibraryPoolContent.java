package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Many-to-one joining entity to specify the contents of a LibraryPool. Links to
 * either a LibraryPrepBatch or a LibraryPool which is pooled by a LibraryPool.
 */
@Entity
@Table(name = "LibraryPoolContents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPoolContent implements DinaEntity {

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
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "librarypoolid")
    }))
  private LibraryPool libraryPool;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "pooledlibraryprepbatchid")
    }))
  private LibraryPrepBatch pooledLibraryPrepBatch;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "pooledlibrarypoolid")
    }))
  private LibraryPool pooledLibraryPool;

  @Transient
  @Override
  public String getGroup() {
    return this.getLibraryPool().getGroup();
  }

}
