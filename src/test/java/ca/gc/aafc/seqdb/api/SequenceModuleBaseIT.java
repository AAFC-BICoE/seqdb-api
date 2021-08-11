package ca.gc.aafc.seqdb.api;

import javax.inject.Inject;

import ca.gc.aafc.seqdb.api.service.PcrBatchItemService;

public class SequenceModuleBaseIT extends BaseIntegrationTest {

  @Inject
  protected PcrBatchItemService pcrBatchItemService;
  
}
