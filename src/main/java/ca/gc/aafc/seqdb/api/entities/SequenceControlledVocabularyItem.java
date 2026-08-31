package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.dina.entity.ControlledVocabularyItem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity(name = "controlled_vocabulary_item")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class SequenceControlledVocabularyItem extends ControlledVocabularyItem {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = CONTROLLED_VOCABULARY_COL_NAME)
  private SequenceControlledVocabulary controlledVocabulary;

}
