package ca.gc.aafc.seqdb.api.dwc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

@Data
@XmlRootElement(name = "extension", namespace = "http://rs.gbif.org/extension/")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@XmlAccessorType(XmlAccessType.FIELD)
public class Extension {

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private String title;

  @XmlAttribute
  private String name;

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private Date issued;

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private String subject;

  @XmlAttribute(namespace = "http://purl.org/dc/terms/")
  private String description;

  @XmlElement(name = "property", namespace = "http://rs.gbif.org/extension/")
  private List<Property> properties = new ArrayList<Property>();

  @XmlTransient
  public Map<String, Property> getPropertyMap() {
    return properties.stream()
      .collect(Collectors.toMap(Property::getName, Function.identity()));
  }
}
