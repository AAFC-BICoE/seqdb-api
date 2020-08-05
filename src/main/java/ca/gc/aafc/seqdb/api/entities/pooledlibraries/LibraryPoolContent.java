package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;


import lombok.Builder;

/**
 * Many-to-one joining entity to specify the contents of a LibraryPool. Links to either a
 * LibraryPrepBatch or a LibraryPool which is pooled by a LibraryPool.
 */
@Entity

@Table(name = "LibraryPoolContents")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class LibraryPoolContent implements Serializable {

  private static final long serialVersionUID = 1566766586169617857L;

  private Integer libraryPoolContentId;

  private LibraryPool libraryPool;

  private LibraryPrepBatch pooledLibraryPrepBatch;

  private LibraryPool pooledLibraryPool;

  public LibraryPoolContent() {
  }

  @Builder
  public LibraryPoolContent(LibraryPool libraryPool, LibraryPrepBatch pooledLibraryPrepBatch,
      LibraryPool pooledLibraryPool) {
    super();
    this.libraryPool = libraryPool;
    this.pooledLibraryPrepBatch = pooledLibraryPrepBatch;
    this.pooledLibraryPool = pooledLibraryPool;
  }

  @Id
  @GeneratedValue(generator = "library-pool-content-sequence-generator")
  @GenericGenerator(name = "library-pool-content-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "librarypoolcontents_librarypoolcontentid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "10") })
  @Column(name = "LibraryPoolContentID")
  public Integer getLibraryPoolContentId() {
    return libraryPoolContentId;
  }

  public void setLibraryPoolContentId(Integer libraryPoolContentId) {
    this.libraryPoolContentId = libraryPoolContentId;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LibraryPoolID")
  public LibraryPool getLibraryPool() {
    return libraryPool;
  }

  public void setLibraryPool(LibraryPool libraryPool) {
    this.libraryPool = libraryPool;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PooledLibraryPrepBatchID")
  public LibraryPrepBatch getPooledLibraryPrepBatch() {
    return pooledLibraryPrepBatch;
  }

  public void setPooledLibraryPrepBatch(LibraryPrepBatch pooledLibraryPrepBatch) {
    this.pooledLibraryPrepBatch = pooledLibraryPrepBatch;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PooledLibraryPoolID")
  public LibraryPool getPooledLibraryPool() {
    return pooledLibraryPool;
  }

  public void setPooledLibraryPool(LibraryPool pooledLibraryPool) {
    this.pooledLibraryPool = pooledLibraryPool;
  }

  @Transient
  public Integer getId() {
    return libraryPoolContentId;
  }

  public void setId(Integer id) {
    this.libraryPoolContentId = id;
  }

}
