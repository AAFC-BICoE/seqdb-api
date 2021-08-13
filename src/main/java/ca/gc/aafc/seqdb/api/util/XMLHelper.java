package ca.gc.aafc.seqdb.api.util;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public final class XMLHelper {

  private XMLHelper() {
    // utility class
  }

  @SuppressWarnings("unchecked")
  public static <T> T readXML(InputStream stream, Class<T> clazz) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return (T) jaxbUnmarshaller.unmarshal(stream);
  }
  
}
