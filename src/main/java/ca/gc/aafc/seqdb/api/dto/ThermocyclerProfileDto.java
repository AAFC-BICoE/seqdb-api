package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "thermocyclerprofile")
public class ThermocyclerProfileDto {
  
  @JsonApiId
  private Integer pcrProfileId;
  
  private String name;
  
  private String cycles;
  
  private Map<Integer, String> steps = new HashMap<Integer, String>();
  
  private Timestamp lastModified;
  
  @JsonApiRelation
  private RegionDto region;

  @JsonApiRelation
  private GroupDto group;
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep1(String value) {
    this.steps.put(1, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep2(String value) {
    this.steps.put(2 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep3(String value) {
    this.steps.put(3 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep4(String value) {
    this.steps.put(4 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep5(String value) {
    this.steps.put(5 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep6(String value) {
    this.steps.put(6 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep7(String value) {
    this.steps.put(7 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep8(String value) {
    this.steps.put(8 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep9(String value) {
    this.steps.put(9 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep10(String value) {
    this.steps.put(10 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep11(String value) {
    this.steps.put(11 , value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep12(String value) {
    this.steps.put(12, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep13(String value) {
    this.steps.put(13, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep14(String value) {
    this.steps.put(14, value);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @param The value string you want to place with predefined key
   */
  public void putInStep15(String value) {
    this.steps.put(15, value);
  }
  
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep1() {
    return this.steps.get(1);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep2() {
    return this.steps.get(2);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep3() {
    return this.steps.get(3);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep4() {
    return this.steps.get(4);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep5() {
    return this.steps.get(5);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep6() {
    return this.steps.get(6);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep7() {
    return this.steps.get(7);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep8() {
    return this.steps.get(8);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep9() {
    return this.steps.get(9);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep10() {
    return this.steps.get(10);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep11() {
    return this.steps.get(11);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep12() {
    return this.steps.get(12);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */ 
  public String returnStep13() {
    return this.steps.get(13);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep14() {
    return this.steps.get(14);
  }
  
  /**
   * EDSL method to access the steps Map
   * This implementation allows for mapping instructions
   * to interact with Map<Integer, String> steps 
   * @return String value of predefined key
   */
  public String returnStep15() {
    return this.steps.get(15);
  }
  
  
  
  
}
