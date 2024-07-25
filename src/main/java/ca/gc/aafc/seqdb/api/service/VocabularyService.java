package ca.gc.aafc.seqdb.api.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.service.CollectionBackedReadOnlyDinaService;
import ca.gc.aafc.seqdb.api.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.dto.VocabularyDto;

@Service
public class VocabularyService extends CollectionBackedReadOnlyDinaService<String, VocabularyDto> {

  public VocabularyService(SequenceVocabularyConfiguration sequenceVocabularyConfiguration) {
    super(sequenceVocabularyConfiguration.getVocabulary()
      .entrySet()
      .stream()
      .map(entry -> new VocabularyDto(entry.getKey(), entry.getValue()))
      .collect(Collectors.toList()), VocabularyDto::getId);
  }

  }
