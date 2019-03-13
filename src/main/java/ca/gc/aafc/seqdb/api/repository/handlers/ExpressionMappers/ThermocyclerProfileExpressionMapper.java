package ca.gc.aafc.seqdb.api.repository.handlers.ExpressionMappers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class ThermocyclerProfileExpressionMapper {
  
  //hiding the constructor since this class should only have static methods
  private ThermocyclerProfileExpressionMapper() {}

  public static List<List<String>> mapSelectedFieldsForPcrProfile(
      List<List<String>> selectedFields) {

    List<String> fields = new ArrayList<String>();
    fields.add("steps");

    ListIterator<List<String>> iter = selectedFields.listIterator();

    while (iter.hasNext()) {
      List<String> next = iter.next();
      if (next.equals(fields)) {

        iter.remove();

        List<String> step1 = new ArrayList<String>();
        step1.add("step1");
        iter.add(step1);

        List<String> step2 = new ArrayList<String>();
        step2.add("step2");
        iter.add(step2);

        List<String> step3 = new ArrayList<String>();
        step3.add("step3");
        iter.add(step3);

        List<String> step4 = new ArrayList<String>();
        step4.add("step4");
        iter.add(step4);

        List<String> step5 = new ArrayList<String>();
        step5.add("step5");
        iter.add(step5);

        List<String> step6 = new ArrayList<String>();
        step6.add("step6");
        iter.add(step6);

        List<String> step7 = new ArrayList<String>();
        step7.add("step7");
        iter.add(step7);
        List<String> step8 = new ArrayList<String>();
        step8.add("step8");
        iter.add(step8);

        List<String> step9 = new ArrayList<String>();
        step9.add("step9");
        iter.add(step9);

        List<String> step10 = new ArrayList<String>();
        step10.add("step10");
        iter.add(step10);

        List<String> step11 = new ArrayList<String>();
        step11.add("step11");
        iter.add(step11);

        List<String> step12 = new ArrayList<String>();
        step12.add("step12");
        iter.add(step12);

        List<String> step13 = new ArrayList<String>();
        step13.add("step13");
        iter.add(step13);

        List<String> step14 = new ArrayList<String>();
        step14.add("step14");
        iter.add(step14);

        List<String> step15 = new ArrayList<String>();
        step15.add("step15");
        iter.add(step15);

      }
    }
  return selectedFields;
  }

}
