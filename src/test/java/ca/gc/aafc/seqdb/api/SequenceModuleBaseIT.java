package ca.gc.aafc.seqdb.api;

import javax.inject.Inject;

import ca.gc.aafc.seqdb.api.service.MolecularAnalysisRunService;
import ca.gc.aafc.seqdb.api.service.PcrBatchItemService;
import ca.gc.aafc.seqdb.api.service.PcrBatchService;
import ca.gc.aafc.seqdb.api.service.SeqSubmissionService;
import ca.gc.aafc.seqdb.api.service.SequencingFacilityService;

public class SequenceModuleBaseIT extends BaseIntegrationTest {

  @Inject
  protected PcrBatchService pcrBatchService;

  @Inject
  protected PcrBatchItemService pcrBatchItemService;

  @Inject 
  protected SeqSubmissionService seqSubmissionService;

  @Inject
  protected SequencingFacilityService sequencingFacilityService;

  @Inject
  protected MolecularAnalysisRunService molecularAnalysisRunService;
  
}
