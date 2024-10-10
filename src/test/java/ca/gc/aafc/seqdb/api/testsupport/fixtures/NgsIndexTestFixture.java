package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import java.util.Random;

import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex.NgsIndexDirection;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class NgsIndexTestFixture {
    
    public static NgsIndexDto newNgsIndex() {
        NgsIndexDto ngsIndexDto = new NgsIndexDto();

        Random rand = new Random();

        ngsIndexDto.setCreatedBy("createdBy");
        ngsIndexDto.setCreatedOn(OffsetDateTime.of(LocalDate.now(), LocalTime.now(), ZoneOffset.MIN));
        ngsIndexDto.setName(TestableEntityFactory.generateRandomName(10));
        ngsIndexDto.setDirection(NgsIndexDirection.FORWARD);
        ngsIndexDto.setLotNumber(rand.nextInt(1000));
        ngsIndexDto.setPurification("purification");
        ngsIndexDto.setTmCalculated("tmCalculated");
        ngsIndexDto.setDateOrdered(LocalDate.now());
        ngsIndexDto.setDateDestroyed(LocalDate.now());
        ngsIndexDto.setApplication("application");
        ngsIndexDto.setReference("reference");
        ngsIndexDto.setSupplier("supplier");
        ngsIndexDto.setDesignedBy("designedBy");
        ngsIndexDto.setStockConcentration("stockConcentration");
        ngsIndexDto.setNotes("notes");
        ngsIndexDto.setLitReference("litReference");
        ngsIndexDto.setPrimerSequence("primerSequence");
        ngsIndexDto.setMiSeqHiSeqIndexSequence("miSeqHiSeqIndexSequence");
        ngsIndexDto.setMiniSeqNextSeqIndexSequence("miniSeqNextSeqIndexSequence");
        ngsIndexDto.setIndexSet(null);
        
        return ngsIndexDto;
    }
}
