package ca.gc.aafc.seqdb.api.dwc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.xml.bind.JAXBException;

import ca.gc.aafc.seqdb.api.util.XMLHelper;
import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class DwcExtensionTest {

  public static final String DEFAULT_UNMARSHALLING_FILE = "dwc/mixs_darwin_core_extension.xml";

  @Test
  public void whenReadingDwcExtensionXML_ThenCorrectExtensionIsReturned()
      throws JAXBException, IOException {
    Resource testFile = new ClassPathResource(DEFAULT_UNMARSHALLING_FILE);
    Extension extension = XMLHelper.readXML(testFile.getInputStream(), Extension.class);

    assertEquals("MIxS Darwin Core Extension", extension.getTitle());

    assertEquals(80, extension.getProperties().size());
    assertEquals(80, extension.getPropertyMap().size());

    Property property = extension.getProperty("samp_name");
    assertEquals("samp_name", property.getName());
    assertEquals("", property.getGroup());
    assertEquals("https://w3id.org/gensc/terms/", property.getNamespace());
    assertEquals("https://w3id.org/gensc/terms/MIXS:0001107", property.getQualName());
    assertEquals("", property.getRelation());
    assertEquals("\"Sample Name is a name that you choose for the sample. It can have any format, but we suggest that you make it concise, unique and consistent within your lab, and as informative as possible. Every Sample Name from a single Submitter must be unique. \"", property.getDescription());
    assertEquals("", property.getExamples());
    assertFalse(property.isRequired());

  }
  
}
