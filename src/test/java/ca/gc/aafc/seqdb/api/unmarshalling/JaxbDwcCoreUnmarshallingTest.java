package ca.gc.aafc.seqdb.api.unmarshalling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.Test;

public class JaxbDwcCoreUnmarshallingTest {

  @Test
  public void whenUnmarshalIsCalled_ThenCorrectExtensionIsReturned() throws JAXBException, DatatypeConfigurationException {
      InputStream inputStream = JaxbDwcCoreUnmarshalling.getInputStream(JaxbDwcCoreUnmarshalling.DEFAULT_UNMARSHALLING_FILE);

      Extension property = JaxbDwcCoreUnmarshalling.unmarshal(inputStream);

      assertEquals("expected", property.toString());
  }
  
}
