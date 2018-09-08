package ca.gc.aafc.seqdb.api.security.authorization;

import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Group;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Convenience class to create instances of ReadableGroupFilterHandler without having to provide all
 * of its dependencies.
 */
@Named
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ReadableGroupFilterHandlerFactory {

  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final AccountRepository accountRepository;
  
  /**
   * Create a new ReadableGroupFilterHandler.
   * 
   * @param pathToGroup JPA path to the group that the resource belongs to.
   * @return new ReadableGroupFilterHandler.
   */
  public ReadableGroupFilterHandler create(Function<From<?, ?>, Path<Group>> pathToGroup) {
    return new ReadableGroupFilterHandler(entityManager, accountRepository, pathToGroup);
  }
  
}
