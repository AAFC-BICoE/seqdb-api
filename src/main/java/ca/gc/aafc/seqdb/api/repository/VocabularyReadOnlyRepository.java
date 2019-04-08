package ca.gc.aafc.seqdb.api.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ReadOnlyResourceRepositoryBase;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;

public class VocabularyReadOnlyRepository extends ReadOnlyResourceRepositoryBase<BaseVocabularyDto, Serializable>{
  
  
  private final static String ENTITY_FILTER = "ca/gc/aafc/seqdb/entities/";
  private final static String JAR_LOCATION = "lib/seqdb.dbi-3.30-SNAPSHOT.jar";
  
  public BaseVocabularyDto baseVocabulary;
  
  public static Set<Class<?>> exposedEntityClasses = new HashSet<Class<?>>() {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    {
      add(Region.class);
      add(PcrPrimer.class);
      add(PcrBatch.class);
      add(PcrReaction.class);
      add(Group.class);
      add(Product.class);
    }
  };
  
  private static Set<Class<?>> getEnumClasses(Set<Class<?>> classesToExpose) throws IOException, ClassNotFoundException {
    
    Set<Class<?>> resultList = new HashSet<Class<?>>();

    File f = new File(JAR_LOCATION);
    FileInputStream fis = new FileInputStream(f);
    JarInputStream jis = new JarInputStream(fis);

    try {
      JarEntry next = jis.getNextJarEntry();
      while (next != null) {

        final String name = next.getName();

        if (name.endsWith(".class") && name.contains(ENTITY_FILTER)) {
          String classname = name.replace('/', '.').substring(0, name.length() - 6);
          Class<?> entityClass = Class.forName(classname);
          if (entityClass.isEnum() && classesToExpose.contains(entityClass.getDeclaringClass())) {
            resultList.add(entityClass);
          }
        }

        next = jis.getNextJarEntry();
      }
    } finally {
      jis.close();
    }
    
    return resultList;
  }
  
  private static Map<String, Object[]> getVocabularies() {
    Map<String, Object[]> resultList = new HashMap<String, Object[]>();
    
    Set<Class<?>> enumClasses = null;
    try {
      enumClasses = getEnumClasses(exposedEntityClasses);
 
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
    if (enumClasses != null) {
      for(Class<?> clazz : enumClasses) {
        
        String entryName = clazz.getSimpleName();
        Object[] enumConstantsArray = clazz.getEnumConstants();
        
        resultList.put(entryName, enumConstantsArray);
      }
    }

    return resultList;
  }
  

  
  
  public VocabularyReadOnlyRepository() {
    super(BaseVocabularyDto.class);
    this.baseVocabulary = new BaseVocabularyDto(getVocabularies());
    

  }

  @Override
  public ResourceList<BaseVocabularyDto> findAll(QuerySpec querySpec) {
    ArrayList<BaseVocabularyDto> resultList = new ArrayList<BaseVocabularyDto>();
    resultList.add(baseVocabulary);

    return new DefaultResourceList<BaseVocabularyDto>(resultList, null, null);
  }
  
  
  

    
}
