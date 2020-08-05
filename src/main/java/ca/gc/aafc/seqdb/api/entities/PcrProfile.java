/*
 * =====================================================================
 * Class:    	PCRProfile.java
 * Package:		ca.gc.aafc.seqdb.api.entities
 *
 * Author:  Satpal Bilkhu
 * Date:    Feb 22, 2010
 * Contact: bilkhus@agr.gc.ca
 *
 *
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Agriculture and Agri-Food Canada http://www.agr.gc.ca/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====================================================================
 * 
 */
package ca.gc.aafc.seqdb.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The Class PcrProfile.
 */
@Entity

@Table(name = "PcrProfiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name", "GroupID" }) })
public class PcrProfile implements  RestrictedByGroup {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 2472713180924441324L;

  /** The pcr profile id. */
  private Integer pcrProfileId;

  /** The name. */
  private String name;

  /** The application. */
  private String application;

  /** The cycles. */
  private String cycles;

  /** The step1. */
  private String step1;

  /** The step2. */
  private String step2;

  /** The step3. */
  private String step3;

  /** The step4. */
  private String step4;

  /** The step5. */
  private String step5;

  /** The step6. */
  private String step6;

  /** The step7. */
  private String step7;

  /** The step8. */
  private String step8;

  /** The step9. */
  private String step9;

  /** The step10. */
  private String step10;

  /** The step11. */
  private String step11;

  /** The step12. */
  private String step12;

  /** The step13. */
  private String step13;

  /** The step14. */
  private String step14;

  /** The step15. */
  private String step15;

  /** The last modified. */
  private Timestamp lastModified;

  /** The region. */
  private Region region;

  /** The group. */
  private Group group;

  /**
   * Instantiates a new pcr profile.
   */
  public PcrProfile() {
    super();
  }

  /**
   * Instantiates a new pcr profile.
   *
   * @param name
   *          the name
   * @param region
   *          the region
   * @param group
   *          the group
   */
  public PcrProfile(String name, Region region, Group group) {
    super();
    this.name = name;
    this.region = region;
    this.group = group;
  }

  /**
   * Instantiates a new pcr profile.
   *
   * @param pcrProfileId
   *          the pcr profile id
   * @param name
   *          the name
   * @param application
   *          the application
   * @param cycles
   *          the cycles
   * @param step1
   *          the step1
   * @param step2
   *          the step2
   * @param step3
   *          the step3
   * @param step4
   *          the step4
   * @param step5
   *          the step5
   * @param step6
   *          the step6
   * @param step7
   *          the step7
   * @param step8
   *          the step8
   * @param step9
   *          the step9
   * @param step10
   *          the step10
   * @param step11
   *          the step11
   * @param step12
   *          the step12
   * @param step13
   *          the step13
   * @param step14
   *          the step14
   * @param step15
   *          the step15
   * @param region
   *          the region
   * @param group
   *          the group
   */
  @Builder
  public PcrProfile(Integer pcrProfileId, String name, String application, String cycles,
      String step1, String step2, String step3, String step4, String step5, String step6,
      String step7, String step8, String step9, String step10, String step11, String step12,
      String step13, String step14, String step15, Region region, Group group) {
    super();
    this.pcrProfileId = pcrProfileId;
    this.name = name;
    this.application = application;
    this.cycles = cycles;
    this.step1 = step1;
    this.step2 = step2;
    this.step3 = step3;
    this.step4 = step4;
    this.step5 = step5;
    this.step6 = step6;
    this.step7 = step7;
    this.step8 = step8;
    this.step9 = step9;
    this.step10 = step10;
    this.step11 = step11;
    this.step12 = step12;
    this.step13 = step13;
    this.step14 = step14;
    this.step15 = step15;
    this.region = region;
    this.group = group;
  }

  // Property accessors
  /**
   * Gets the pcr profile id.
   *
   * @return the pcr profile id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PCRProfileID")
  public Integer getPcrProfileId() {
    return pcrProfileId;
  }

  /**
   * Sets the pcr profile id.
   *
   * @param pcrProfileId
   *          the new pcr profile id
   */
  public void setPcrProfileId(Integer pcrProfileId) {
    this.pcrProfileId = pcrProfileId;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the application.
   *
   * @return the application
   */
  @Size(max = 1024)
  @Column(name = "Application")
  public String getApplication() {
    return application;
  }

  /**
   * Sets the application.
   *
   * @param application
   *          the new application
   */
  public void setApplication(String application) {
    this.application = application;
  }

  /**
   * Gets the cycles.
   *
   * @return the cycles
   */
  @Size(max = 50)
  @Column(name = "Cycles")
  public String getCycles() {
    return cycles;
  }

  /**
   * Sets the cycles.
   *
   * @param cycles
   *          the new cycles
   */
  public void setCycles(String cycles) {
    this.cycles = cycles;
  }

  /**
   * Gets the step1.
   *
   * @return the step1
   */
  @Size(max = 50)
  @Column(name = "Step1")
  public String getStep1() {
    return step1;
  }

  /**
   * Sets the step1.
   *
   * @param step1
   *          the new step1
   */
  public void setStep1(String step1) {
    this.step1 = step1;
  }

  /**
   * Gets the step2.
   *
   * @return the step2
   */
  @Size(max = 50)
  @Column(name = "Step2")
  public String getStep2() {
    return step2;
  }

  /**
   * Sets the step2.
   *
   * @param step2
   *          the new step2
   */
  public void setStep2(String step2) {
    this.step2 = step2;
  }

  /**
   * Gets the step3.
   *
   * @return the step3
   */
  @Size(max = 50)
  @Column(name = "Step3")
  public String getStep3() {
    return step3;
  }

  /**
   * Sets the step3.
   *
   * @param step3
   *          the new step3
   */
  public void setStep3(String step3) {
    this.step3 = step3;
  }

  /**
   * Gets the step4.
   *
   * @return the step4
   */
  @Size(max = 50)
  @Column(name = "Step4")
  public String getStep4() {
    return step4;
  }

  /**
   * Sets the step4.
   *
   * @param step4
   *          the new step4
   */
  public void setStep4(String step4) {
    this.step4 = step4;
  }

  /**
   * Gets the step5.
   *
   * @return the step5
   */
  @Size(max = 50)
  @Column(name = "Step5")
  public String getStep5() {
    return step5;
  }

  /**
   * Sets the step5.
   *
   * @param step5
   *          the new step5
   */
  public void setStep5(String step5) {
    this.step5 = step5;
  }

  /**
   * Gets the step6.
   *
   * @return the step6
   */
  @Size(max = 50)
  @Column(name = "Step6")
  public String getStep6() {
    return step6;
  }

  /**
   * Sets the step6.
   *
   * @param step6
   *          the new step6
   */
  public void setStep6(String step6) {
    this.step6 = step6;
  }

  /**
   * Gets the step7.
   *
   * @return the step7
   */
  @Size(max = 50)
  @Column(name = "Step7")
  public String getStep7() {
    return step7;
  }

  /**
   * Sets the step7.
   *
   * @param step7
   *          the new step7
   */
  public void setStep7(String step7) {
    this.step7 = step7;
  }

  /**
   * Gets the step8.
   *
   * @return the step8
   */
  @Size(max = 50)
  @Column(name = "Step8")
  public String getStep8() {
    return step8;
  }

  /**
   * Sets the step8.
   *
   * @param step8
   *          the new step8
   */
  public void setStep8(String step8) {
    this.step8 = step8;
  }

  /**
   * Gets the step9.
   *
   * @return the step9
   */
  @Size(max = 50)
  @Column(name = "Step9")
  public String getStep9() {
    return step9;
  }

  /**
   * Sets the step9.
   *
   * @param step9
   *          the new step9
   */
  public void setStep9(String step9) {
    this.step9 = step9;
  }

  /**
   * Gets the step10.
   *
   * @return the step10
   */
  @Size(max = 50)
  @Column(name = "Step10")
  public String getStep10() {
    return step10;
  }

  /**
   * Sets the step10.
   *
   * @param step10
   *          the new step10
   */
  public void setStep10(String step10) {
    this.step10 = step10;
  }

  /**
   * Gets the step11.
   *
   * @return the step11
   */
  @Size(max = 50)
  @Column(name = "Step11")
  public String getStep11() {
    return step11;
  }

  /**
   * Sets the step11.
   *
   * @param step11
   *          the new step11
   */
  public void setStep11(String step11) {
    this.step11 = step11;
  }

  /**
   * Gets the step12.
   *
   * @return the step12
   */
  @Size(max = 50)
  @Column(name = "Step12")
  public String getStep12() {
    return step12;
  }

  /**
   * Sets the step12.
   *
   * @param step12
   *          the new step12
   */
  public void setStep12(String step12) {
    this.step12 = step12;
  }

  /**
   * Gets the step13.
   *
   * @return the step13
   */
  @Size(max = 50)
  @Column(name = "Step13")
  public String getStep13() {
    return step13;
  }

  /**
   * Sets the step13.
   *
   * @param step13
   *          the new step13
   */
  public void setStep13(String step13) {
    this.step13 = step13;
  }

  /**
   * Gets the step14.
   *
   * @return the step14
   */
  @Size(max = 50)
  @Column(name = "Step14")
  public String getStep14() {
    return step14;
  }

  /**
   * Sets the step14.
   *
   * @param step14
   *          the new step14
   */
  public void setStep14(String step14) {
    this.step14 = step14;
  }

  /**
   * Gets the step15.
   *
   * @return the step15
   */
  @Size(max = 50)
  @Column(name = "Step15")
  public String getStep15() {
    return step15;
  }

  /**
   * Sets the step15.
   *
   * @param step15
   *          the new step15
   */
  public void setStep15(String step15) {
    this.step15 = step15;
  }

  /**
   * Gets the last modified.
   *
   * @return the last modified
   */
  @Version
  @Column(name = "LastModified")
  public Timestamp getLastModified() {
    return lastModified;
  }

  /*
   * Note that with @Version defined for the lastModified field, every time this object is created
   * or modified, lastModified is set automatically, so no explicit set is needed for this field.
   */
  /**
   * Sets the last modified.
   *
   * @param lastModified
   *          the new last modified
   */
  public void setLastModified(Timestamp lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Gets the region.
   *
   * @return the region
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RegionID")
  public Region getRegion() {
    return region;
  }

  /**
   * Sets the region.
   *
   * @param region
   *          the new region
   */
  public void setRegion(Region region) {
    this.region = region;
  }

  /**
   * Gets the group.
   *
   * @return the group
   */
  
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "GroupID")
  public Group getGroup() {
    return group;
  }

  /**
   * Sets the group.
   *
   * @param group
   *          the new group
   */
  
  public void setGroup(Group group) {
    this.group = group;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return getPcrProfileId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setPcrProfileId(id);
  }

  @Override
  @Transient
  @JsonIgnore
  public Group getAccessGroup() {
    return getGroup();
  }

}
