package ca.gc.aafc.seqdb.api.repository.handlers.ExpressionMappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;

public class ExpressionMapper {

  public final static List<Class<?>> listOfDtoWithMaps = new ArrayList<Class<?>>() {

    private static final long serialVersionUID = 2657957841070148686L;

    {
      add(ThermocyclerProfileDto.class);

    }
  };

  public static Map<Class<?>, List<List<String>>> evaluateSelections(
      Map<Class<?>, List<List<String>>> selectedFields) {

    for (Class<?> clazz : listOfDtoWithMaps) {
      if (selectedFields.containsKey(clazz)) {

        List<List<String>> newSelectedFields = selectedFields.get(clazz);

        if (clazz == ThermocyclerProfileDto.class) {
          selectedFields.replace(ThermocyclerProfileDto.class, 
              ThermocyclerProfileExpressionMapper.mapSelectedFieldsForPcrProfile(newSelectedFields));

        } else {
          throw new IllegalStateException("Could not find mapper for" + clazz.getSimpleName() + "Please add it to the list of cases");
        }

      }

    }

    return selectedFields;

  }

}
