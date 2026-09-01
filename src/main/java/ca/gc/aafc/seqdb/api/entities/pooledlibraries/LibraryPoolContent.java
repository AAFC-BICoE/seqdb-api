package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Many-to-one joining entity to specify the contents of a LibraryPool. Links to
 * either a LibraryPrepBatch or a LibraryPool which is pooled by a LibraryPool.
 */
@Entity
@Table(name = "librarypoolcontents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPoolContent implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby", updatable = false)
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "librarypoolid")
  private LibraryPool libraryPool;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pooledlibraryprepbatchid")
  private LibraryPrepBatch pooledLibraryPrepBatch;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pooledlibrarypoolid")
  private LibraryPool pooledLibraryPool;

  @Transient
  @Override
  public String getGroup() {
    return this.getLibraryPool().getGroup();
  }

}
