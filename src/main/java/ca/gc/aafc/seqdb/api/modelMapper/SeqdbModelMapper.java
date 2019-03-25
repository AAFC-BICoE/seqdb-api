package ca.gc.aafc.seqdb.api.modelMapper;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.entities.PcrProfile;

public class SeqdbModelMapper {
  
  
  public static ModelMapper getConfiguredMapper() {
    ModelMapper mapper = new ModelMapper();
    
    mapper.addMappings(new PropertyMap<ThermocyclerProfileDto, PcrProfile>(){

      @Override
      protected void configure() {
        map().setStep1(source.returnStep1());

        map().setStep2(source.returnStep2());

        map().setStep3(source.returnStep3());

        map().setStep4(source.returnStep4());

        map().setStep5(source.returnStep5());

        map().setStep6(source.returnStep6());

        map().setStep7(source.returnStep7());

        map().setStep8(source.returnStep8());

        map().setStep9(source.returnStep9());

        map().setStep10(source.returnStep10());

        map().setStep11(source.returnStep11());

        map().setStep12(source.returnStep12());

        map().setStep13(source.returnStep13());

        map().setStep14(source.returnStep14());

        map().setStep15(source.returnStep15());
        
      }
      
    });
    
    mapper.addMappings(new PropertyMap<PcrProfile, ThermocyclerProfileDto>(){

      @Override
      protected void configure() {
        map().putInStep1( source.getStep1());

        map().putInStep2( source.getStep2());

        map().putInStep3(  source.getStep3());

        map().putInStep4( source.getStep4());

        map().putInStep5( source.getStep5());

        map().putInStep6( source.getStep6());

        map().putInStep7( source.getStep7());

        map().putInStep8( source.getStep8());

        map().putInStep9( source.getStep9());

        map().putInStep10( source.getStep10());

        map().putInStep11( source.getStep11());

        map().putInStep12( source.getStep12());

        map().putInStep13( source.getStep13());

        map().putInStep14( source.getStep14());

        map().putInStep15(source.getStep15());
        
        
      }
      
      
    });
    
    mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    return mapper;
    
  }

}
