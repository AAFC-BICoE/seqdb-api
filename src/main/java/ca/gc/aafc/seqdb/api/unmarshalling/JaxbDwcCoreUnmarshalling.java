package ca.gc.aafc.seqdb.api.unmarshalling;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public final class JaxbDwcCoreUnmarshalling {

  public static final String DEFAULT_UNMARSHALLING_FILE = "mixs_darwin_core_extension.xml";

  public static Extension unmarshal(InputStream inputFile) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(Extension.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return (Extension) jaxbUnmarshaller.unmarshal(inputFile);
  }

  public static InputStream getInputStream(String file) {
    ClassLoader classLoader = JaxbDwcCoreUnmarshalling.class.getClassLoader();
    return classLoader.getResourceAsStream(file);
  }

  private JaxbDwcCoreUnmarshalling() {
    
  }
  
}
