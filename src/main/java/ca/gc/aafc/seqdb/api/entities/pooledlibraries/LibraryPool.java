package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Builder;

@Entity

@Table(name = "LibraryPools")
public class LibraryPool implements Serializable {

  private static final long serialVersionUID = -5278505617657013701L;

  private Integer libraryPoolId;

  private String name;

  private LocalDate dateUsed;

  private String notes;

  private List<LibraryPoolContent> contents;

  public LibraryPool() {
  }

  @Builder
  public LibraryPool(String name, LocalDate dateUsed, String notes) {
    super();
    this.name = name;
    this.dateUsed = dateUsed;
    this.notes = notes;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LibraryPoolID")
  public Integer getLibraryPoolId() {
    return libraryPoolId;
  }

  public void setLibraryPoolId(Integer libraryPoolId) {
    this.libraryPoolId = libraryPoolId;
  }

  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "DateUsed")
  public LocalDate getDateUsed() {
    return dateUsed;
  }

  public void setDateUsed(LocalDate dateUsed) {
    this.dateUsed = dateUsed;
  }

  @Column(name = "Notes")
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @OneToMany(mappedBy = "libraryPool")
  public List<LibraryPoolContent> getContents() {
    return contents;
  }

  public void setContents(List<LibraryPoolContent> contents) {
    this.contents = contents;
  }

  @Transient
  public Integer getId() {
    return libraryPoolId;
  }

  public void setId(Integer id) {
    this.libraryPoolId = id;
  }

}
