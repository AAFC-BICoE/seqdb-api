package ca.gc.aafc.seqdb.api.dwc;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

@Getter
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

  @XmlAttribute
  private String group;

  @XmlAttribute
  private String name;

  @XmlAttribute
  private String namespace;

  @XmlAttribute
  private String qualName;

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private String relation;

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private String description;

  @XmlAttribute
  private String examples;

  @XmlAttribute
  private boolean required;

  @XmlAttribute
  private String type;

  @XmlAttribute
  private String thesaurus;
}
