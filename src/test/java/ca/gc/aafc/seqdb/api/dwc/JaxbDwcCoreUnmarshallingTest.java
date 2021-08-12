package ca.gc.aafc.seqdb.api.dwc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dwc.Extension;
import ca.gc.aafc.seqdb.api.dwc.JaxbDwcCoreUnmarshalling;
import ca.gc.aafc.seqdb.api.dwc.Property;

public class JaxbDwcCoreUnmarshallingTest {

  @Test
  public void whenUnmarshalIsCalled_ThenCorrectExtensionIsReturned() throws JAXBException, DatatypeConfigurationException {
      InputStream inputStream = JaxbDwcCoreUnmarshalling.getInputStream(JaxbDwcCoreUnmarshalling.DEFAULT_UNMARSHALLING_FILE);

      Extension extension = JaxbDwcCoreUnmarshalling.unmarshal(inputStream, Extension.class);

      assertEquals(80, extension.getProperties().size());
      assertEquals(80, extension.getPropertyMap().size());

      Property property = extension.getPropertyMap().get("samp_name");
      assertEquals("samp_name", property.getName());
      assertEquals("", property.getGroup());
      assertEquals("https://w3id.org/gensc/terms/", property.getNamespace());
      assertEquals("https://w3id.org/gensc/terms/MIXS:0001107", property.getQualName());
      assertEquals("", property.getRelation());
      assertEquals("\"Sample Name is a name that you choose for the sample. It can have any format, but we suggest that you make it concise, unique and consistent within your lab, and as informative as possible. Every Sample Name from a single Submitter must be unique. \"", property.getDescription());
      assertEquals("", property.getExamples());
      assertEquals(false, property.isRequired());

  }
  
}
