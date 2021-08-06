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
  public String group;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String namespace;

  @XmlAttribute
  public String qualName;

  @XmlAttribute(name = "dc:relation")
  public String dcRelation;

  @XmlAttribute(name = "dc:description")
  public String dcDescription;

  @XmlAttribute
  public String examples;

  @XmlAttribute
  public String required;
}
