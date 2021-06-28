package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.time.LocalDate;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class PcrPrimerTestFixture {

  public static PcrPrimerDto newPcrPrimer() {
    PcrPrimerDto pcrPrimerDto = new PcrPrimerDto();
    pcrPrimerDto.setType(PrimerType.PRIMER);
    pcrPrimerDto.setName(TestableEntityFactory.generateRandomName(10));
    pcrPrimerDto.setDirection("F");
    pcrPrimerDto.setLotNumber(1);
    pcrPrimerDto.setSeq("CTTGGTCATTTAGAGGAAGTAA");
    pcrPrimerDto.setGroup("aafc");
    pcrPrimerDto.setCreatedBy("createdBy");
    pcrPrimerDto.setNote("note");
    pcrPrimerDto.setTmCalculated("tm");
    pcrPrimerDto.setStockConcentration("50ppm");
    pcrPrimerDto.setReference("reference");
    pcrPrimerDto.setDesignedBy("dina");
    pcrPrimerDto.setPurification("purification");
    pcrPrimerDto.setSupplier("aafc");
    pcrPrimerDto.setDateOrdered(LocalDate.of(2020, 1, 8));
    pcrPrimerDto.setVersion(1);
    pcrPrimerDto.setApplication("App");
    pcrPrimerDto.setTargetSpecies("corn");
    pcrPrimerDto.setTmPe(5);
    pcrPrimerDto.setPosition("position");
    return pcrPrimerDto;
  }
}
