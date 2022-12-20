package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.entities.SequencingFacility;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

import java.util.List;

public class SequencingFacilityTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SequencingFacilityDto newSequencingFacility() {
    SequencingFacilityDto sequencingFacilityDto = new SequencingFacilityDto();
    sequencingFacilityDto.setGroup(GROUP);
    sequencingFacilityDto.setCreatedBy(CREATED_BY);
    sequencingFacilityDto.setName(TestableEntityFactory.generateRandomNameLettersOnly(8));

    sequencingFacilityDto.setContacts(
            List.of(SequencingFacility.ContactRole.builder()
                    .name("Bob Builder")
                    .roles(List.of("Builder"))
                    .info("How to contact: 111 111 11111")
                    .build())
    );

    sequencingFacilityDto.setShippingAddress(SequencingFacility.Address.builder()
            .addressLine1(TestableEntityFactory.generateRandomName(15))
            .addressLine2(TestableEntityFactory.generateRandomName(10))
            .city(TestableEntityFactory.generateRandomNameLettersOnly(12))
            .provinceState(TestableEntityFactory.generateRandomNameLettersOnly(8))
            .zipCode(TestableEntityFactory.generateRandomName(6))
            .country(TestableEntityFactory.generateRandomNameLettersOnly(5))
            .build()
    );

    return sequencingFacilityDto;
  }

}
