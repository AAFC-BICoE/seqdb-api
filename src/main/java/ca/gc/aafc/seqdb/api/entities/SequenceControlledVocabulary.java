package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.dina.entity.ControlledVocabulary;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "controlled_vocabulary")
@SuperBuilder
@RequiredArgsConstructor
public class SequenceControlledVocabulary extends ControlledVocabulary {

}
