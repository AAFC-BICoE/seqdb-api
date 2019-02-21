package ca.gc.aafc.seqdb.api.dto;

import java.util.HashMap;
import java.util.Map;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "thermocyclerprofile")
public class ThermocyclerProfileDto {
  
  @JsonApiId
  private Integer pcrProfileId;
  
  
  private Map<Integer, String> steps = new HashMap<Integer, String>();
  
  
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep1(String value) {
    this.steps.put(1, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep2(String value) {
    this.steps.put(2 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep3(String value) {
    this.steps.put(3 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep4(String value) {
    this.steps.put(4 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep5(String value) {
    this.steps.put(5 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep6(String value) {
    this.steps.put(6 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep7(String value) {
    this.steps.put(7 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep8(String value) {
    this.steps.put(8 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep9(String value) {
    this.steps.put(9 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep10(String value) {
    this.steps.put(10 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep11(String value) {
    this.steps.put(11 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep12(String value) {
    this.steps.put(12, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep13(String value) {
    this.steps.put(13, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep14(String value) {
    this.steps.put(14, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void setStep15(String value) {
    this.steps.put(15, value);
  }
  
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep1() {
    return this.steps.get(1);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep2() {
    return this.steps.get(2);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep3() {
    return this.steps.get(3);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep4() {
    return this.steps.get(4);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep5() {
    return this.steps.get(5);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep6() {
    return this.steps.get(6);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep7() {
    return this.steps.get(7);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep8() {
    return this.steps.get(8);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep9() {
    return this.steps.get(9);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep10() {
    return this.steps.get(10);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep11() {
    return this.steps.get(11);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep12() {
    return this.steps.get(12);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */ 
  public String getStep13() {
    return this.steps.get(13);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep14() {
    return this.steps.get(14);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String getStep15() {
    return this.steps.get(15);
  }
  
  
  
  
}
