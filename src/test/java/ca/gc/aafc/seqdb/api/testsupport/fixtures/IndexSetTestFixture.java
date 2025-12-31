package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class IndexSetTestFixture {
    
    public static IndexSetDto newIndexSet() {
        IndexSetDto indexSetDto = new IndexSetDto();

        indexSetDto.setCreatedBy("createdBy");
        indexSetDto.setGroup("aafc");
        indexSetDto.setName(TestableEntityFactory.generateRandomName(10));
        indexSetDto.setForwardAdapter("forwardAdapter");
        indexSetDto.setReverseAdapter("reverseAdapter");
        indexSetDto.setNgsIndexes(null);
        
        return indexSetDto;
    }
}
