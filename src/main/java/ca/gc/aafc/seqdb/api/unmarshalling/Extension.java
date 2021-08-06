package ca.gc.aafc.seqdb.api.unmarshalling;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@XmlRootElement(name = "extension", namespace = "http://rs.gbif.org/extension/")
@XmlAccessorType(XmlAccessType.FIELD)
public class Extension {
  
  @XmlElement(name = "property")
  public List<Property> properties = new ArrayList<Property>();
}
