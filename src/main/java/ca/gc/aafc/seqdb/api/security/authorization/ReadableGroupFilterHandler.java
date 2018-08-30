package ca.gc.aafc.seqdb.api.security.authorization;

import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Restricts results to those belonging to the user's readable groups or belonging to no group.
 */
@RequiredArgsConstructor
public class ReadableGroupFilterHandler implements FilterHandler {

  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final AccountRepository accountRepository;

  @NonNull
  private final Function<From<?, ?>, Path<Group>> pathToGroup;
  
  @Override
  public Predicate getRestriction(
      QuerySpec querySpec,
      From<?, ?> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb
  ) {
    Path<Group> groupPath = pathToGroup.apply(root);

    // Get the current username, or null if none is found.
    String currentUsername = Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getName)
        .orElse(null);

    Account currentAccount = this.accountRepository.findByAccountNameIgnoreCase(currentUsername);
    
    // If the user is admin, do not restrict results by group.
    if (
        Optional.ofNullable(currentAccount)
            .map(Account::getAccountType)
            .orElse("")
            .equalsIgnoreCase("admin")
    ) {
      // Empty restriction.
      return cb.and();
    }

    // Query for the Groups where this Account has READ access.
    Subquery<AccountsGroup> readableGroupsQuery = query.subquery(AccountsGroup.class);
    Root<AccountsGroup> accountsGroupRoot = readableGroupsQuery.from(AccountsGroup.class);
    readableGroupsQuery.select(accountsGroupRoot.get("group"));
    readableGroupsQuery.where(
        cb.equal(accountsGroupRoot.get("account"), currentAccount),
        cb.like(accountsGroupRoot.get("rights"), "1___")
    );

    return cb.or(
        // Allow results belonging to readable Groups.
        groupPath.in(readableGroupsQuery),
        // Allow results belonging to no Group.
        cb.isNull(groupPath)
    );
  }

}
