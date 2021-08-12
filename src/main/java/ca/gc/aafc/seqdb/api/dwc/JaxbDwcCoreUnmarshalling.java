package ca.gc.aafc.seqdb.api.dwc;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public final class JaxbDwcCoreUnmarshalling {

  public static final String DEFAULT_UNMARSHALLING_FILE = "mixs_darwin_core_extension.xml";

  public static <T> T unmarshal(InputStream inputFile, Class<T> clazz) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return (T) jaxbUnmarshaller.unmarshal(inputFile);
  }

  public static InputStream getInputStream(String file) {
    ClassLoader classLoader = JaxbDwcCoreUnmarshalling.class.getClassLoader();
    return classLoader.getResourceAsStream(file);
  }

  private JaxbDwcCoreUnmarshalling() {
    
  }
  
}
