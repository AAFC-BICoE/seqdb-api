package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.i18n.MultilingualTitle;

public class MultilingualTestFixture {

  public static MultilingualDescription newMultilingualDescription() {
    return MultilingualDescription.builder()
      .descriptions(List.of(MultilingualDescription
        .MultilingualPair.of("en", RandomStringUtils.randomAlphabetic(4))))
      .build();
  }

  public static MultilingualTitle newMultilingualTitle() {
    return MultilingualTitle.builder()
      .titles(List.of(MultilingualTitle
        .MultilingualTitlePair.of("en", RandomStringUtils.randomAlphabetic(4))))
      .build();
  }

}
