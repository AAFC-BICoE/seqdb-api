package ca.gc.aafc.seqdb.monkeyTesting;

import java.util.HashMap;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ca.gc.aafc.seqdb.api.dto.GroupDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrProfile;
import ca.gc.aafc.seqdb.factories.PcrProfileFactory;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MapingTest {
  
  private static final ModelMapper modelMapper = new ModelMapper();
  
  private static void applyPcrThermocyclerProfileMapping() {

    modelMapper.addMappings(new PropertyMap<ThermocyclerProfileDto, PcrProfile>(){

      @Override
      protected void configure() {
        map().setStep1(source.getStep1());

        map().setStep2(source.getStep2());

        map().setStep3(source.getStep3());

        map().setStep4(source.getStep4());

        map().setStep5(source.getStep5());

        map().setStep6(source.getStep6());

        map().setStep7(source.getStep7());

        map().setStep8(source.getStep8());

        map().setStep9(source.getStep9());

        map().setStep10(source.getStep10());

        map().setStep11(source.getStep11());

        map().setStep12(source.getStep12());

        map().setStep13(source.getStep13());

        map().setStep14(source.getStep14());

        map().setStep15(source.getStep15());
        
      }
      
    });
    
    modelMapper.addMappings(new PropertyMap<PcrProfile, ThermocyclerProfileDto>(){

      @Override
      protected void configure() {
        map().setStep1( source.getStep1());

        map().setStep2( source.getStep2());

        map().setStep3(  source.getStep3());

        map().setStep4( source.getStep4());

        map().setStep5( source.getStep5());

        map().setStep6( source.getStep6());

        map().setStep7( source.getStep7());

        map().setStep8( source.getStep8());

        map().setStep9( source.getStep9());

        map().setStep10( source.getStep10());

        map().setStep11( source.getStep11());

        map().setStep12( source.getStep12());

        map().setStep13( source.getStep13());

        map().setStep14( source.getStep14());

        map().setStep15(source.getStep15());
        
        
      }
      
    });
  }
  
  /**
   * Compares the given PcrProfile with it's expected values.
   * @param profileFromDto
   */
  private void validateCreatedPcrProfileSteps(PcrProfile profileFromDto) {
    assertEquals(profileFromDto.getStep1(), "Problems");
    
    assertEquals(profileFromDto.getStep2(), "worthy");

    assertEquals(profileFromDto.getStep3(), "of");

    assertEquals(profileFromDto.getStep4(), "attack");

    assertEquals(profileFromDto.getStep5(), "prove");

    assertEquals(profileFromDto.getStep6(), "their");

    assertEquals(profileFromDto.getStep7(), "worth");

    assertEquals(profileFromDto.getStep8(), "by");

    assertEquals(profileFromDto.getStep9(), "hitting");

    assertEquals(profileFromDto.getStep10(), "back");
    
    assertEquals(profileFromDto.getStep11(), "boom");
    
    assertEquals(profileFromDto.getStep12(), "Got'em");
    
    assertEquals(profileFromDto.getStep13(), null);
    
    assertEquals(profileFromDto.getStep14(), null);
    
    assertEquals(profileFromDto.getStep15(), "last");
  }
  
  @Test
  public void automaticMappingTest() {
    
    //generate a Dto and set it's values
    GroupDto groupDto = new GroupDto();
    groupDto.setDescription("Monkeys");
    groupDto.setGroupName("Pan");
    
    //create a entity from the Dto using modelMapper
    Group groupEntity = modelMapper.map(groupDto, Group.class);
    
    //Test that the values set onto the Dto are passed as expected
    assertEquals(groupDto.getDescription(), groupEntity.getDescription());
    assertEquals(groupDto.getGroupName(), groupEntity.getGroupName());
    
    //Change the entity's values
    groupEntity.setDescription("Pan");
    groupEntity.setGroupName("Simians");
    
    //Create a Dto from new entity
    groupDto = modelMapper.map(groupEntity, GroupDto.class);
    
    assertEquals(groupDto.getDescription(), groupEntity.getDescription());
    assertEquals(groupDto.getGroupName(), groupEntity.getGroupName());
   
  }
  
  @Test
  public void configuredMappingTest() {
    //Configure mapper with our own defined mapping.
    applyPcrThermocyclerProfileMapping();
    
    //Create a new pcrProfile entity
    PcrProfile p = PcrProfileFactory.newPcrProfile()
        .step1("Problems")
        .step2("worthy")
        .step3("of")
        .step4("attack")
        .step5("prove")
        .step6("their")
        .step7("worth")
        .step8("by")
        .step9("hitting")
        .step10("back")
        .step11("boom")
        .step12("Got'em")
        .step15("last").build();
    
    //Create a Dto using the just made pcrProfile entity 
    ThermocyclerProfileDto sahdoods = modelMapper.map(p, ThermocyclerProfileDto.class);
    
    Map<Integer, String> expectedMap = new HashMap<Integer, String>();
    expectedMap.put(1,"Problems" );
    expectedMap.put(2, "worthy");
    expectedMap.put(3, "of" );
    expectedMap.put(4,"attack");
    expectedMap.put(5, "prove");
    expectedMap.put(6, "their");
    expectedMap.put(7, "worth");
    expectedMap.put(8, "by");
    expectedMap.put(9, "hitting");
    expectedMap.put(10, "back");
    expectedMap.put(11,"boom" );
    expectedMap.put(12, "Got'em");
    expectedMap.put(13, null);
    expectedMap.put(14, null);
    expectedMap.put(15, "last");
    
    //Test that the values were mapped as expected
    assertEquals(sahdoods.getSteps(), expectedMap);
    
    //Same thing but reversed.
    PcrProfile profileFromDto = modelMapper.map(sahdoods, PcrProfile.class);
    
    validateCreatedPcrProfileSteps(profileFromDto);
    
  }

}
