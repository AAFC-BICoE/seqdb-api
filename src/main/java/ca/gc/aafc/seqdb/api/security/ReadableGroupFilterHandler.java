package ca.gc.aafc.seqdb.api.security;

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

    // Query for the current Account making the request.
    Subquery<Account> accountQuery = query.subquery(Account.class);
    Root<Account> accountRoot = accountQuery.from(Account.class);
    accountQuery.select(accountRoot);
    accountQuery.where(cb.equal(accountRoot.get("accountName"), currentUsername));

    // Query for the Groups where this Account has READ access.
    Subquery<AccountsGroup> readableGroupsQuery = query.subquery(AccountsGroup.class);
    Root<AccountsGroup> accountsGroupRoot = readableGroupsQuery.from(AccountsGroup.class);
    readableGroupsQuery.select(accountsGroupRoot.get("group"));
    readableGroupsQuery.where(
        cb.equal(accountsGroupRoot.get("account"), accountQuery),
        cb.like(accountsGroupRoot.get("rights"), "1___")
    );

    // Restrict the results to those belonging to readable Groups or no Group.
    return cb.or(
        groupPath.in(readableGroupsQuery),
        cb.isNull(groupPath)
    );
  }

}
