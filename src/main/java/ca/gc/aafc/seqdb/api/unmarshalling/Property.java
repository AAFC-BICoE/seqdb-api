package ca.gc.aafc.seqdb.api.unmarshalling;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
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
