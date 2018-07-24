package ca.gc.aafc.seqdb.api.security;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.handlers.JpaDtoMapper;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountsGroupRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.interfaces.RestrictedByGroup;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.exception.UnauthorizedException;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Intercepts calls to the "create", "save" and "delete" methods of JpaResourceRepository to apply
 * authorization permission checks.
 */
@Named
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Aspect
public class WritableGroupAuthorizationAspect {

  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final AccountRepository accountRepository;
  
  @NonNull
  private final AccountsGroupRepository accountsGroupRepository;
  
  @NonNull
  private final JpaDtoMapper jpaDtoMapper;
  
  /**
   * Intercepts the create "operation" to apply Group-based authorization.
   * @param joinPoint
   * @param result
   */
  @AfterReturning(
      pointcut = "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.create(..))",
      returning = "result"
  )
  public void createInterceptor(JoinPoint joinPoint, Object result) {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    this.requireGroupAccess(
        result,
        ag -> {
          if (ag == null || !ag.hasCreateAccess()) {
            throw new UnauthorizedException(
                "Create access denied to " + repository.getResourceClass().getSimpleName()
                    + " belonging to Group " + ag.getGroup().getGroupName()
            );
          }
        },
        repository.getResourceRegistry()
    );
  }
  
  @Around(
      "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.save(..))"
      + " && args(inputDto)"
  )
  public void saveInterceptor(ProceedingJoinPoint joinPoint, Object inputDto) throws Throwable {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    Consumer<AccountsGroup> handleSavePermissions = ag -> {
      if (ag == null || !ag.hasWriteAccess()) {
        throw new UnauthorizedException(
            "Write access denied to " + inputDto.getClass().getSimpleName()
                + " belonging to Group " + ag.getGroup().getGroupName()
        );
      }
    };
    
    // Require group access before the edit.
    this.requireGroupAccess(
        inputDto,
        handleSavePermissions,
        repository.getResourceRegistry()
    );
    
    // Call the save method
    Object resultDto = joinPoint.proceed(joinPoint.getArgs());
    
    // Require group access after the edit, in case the object's group was changed.
    this.requireGroupAccess(
        resultDto,
        handleSavePermissions,
        repository.getResourceRegistry()
    );
  }
  
  @Before(
      "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.delete(..))"
      + " && args(id)"
  )
  public void deleteInterceptor(JoinPoint joinPoint, Serializable id) {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    this.requireGroupAccess(
        repository.findOne(id, new QuerySpec(repository.getResourceClass())),
        ag -> {
          if (ag == null || !ag.hasDeleteAccess()) {
            throw new UnauthorizedException(
                "Delete access denied to " + repository.getResourceClass().getSimpleName()
                    + " belonging to Group " + ag.getGroup().getGroupName()
            );
          }
        },
        repository.getResourceRegistry()
    );
  }
  
  private void requireGroupAccess(
      Object dto,
      Consumer<AccountsGroup> handlePermissions,
      ResourceRegistry resourceRegistry
  ) {
    String currentUsername = Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getName)
        .orElse(null);

    Account account = accountRepository.findByAccountNameIgnoreCase(currentUsername);
  
    if (account == null) {
      throw new UnauthorizedException("The user must be logged in to write data.");
    }
    
    // Admins automatically get authorization.
    if ("admin".equalsIgnoreCase(account.getAccountType())) {
      return;
    }
  
    Object entity = this.entityManager.find(
        this.jpaDtoMapper.getEntityClassForDto(dto.getClass()),
        resourceRegistry.findEntry(dto.getClass()).getResourceInformation().getId(dto)
    );
    
    // Authorize writes to objects that are not restricted by group.
    if (!(entity instanceof RestrictedByGroup)) {
      return;
    }
    
    RestrictedByGroup restrictedObject = (RestrictedByGroup) entity;
  
    // Get the required group using the RestrictedByGroup interface's access group method.
    Group group = restrictedObject.getAccessGroup();

    // Operation on an entity with no group should be authorized.
    if (group == null) {
      return;
    }

    // Get the permissions object for the current user and the entity's access group.
    AccountsGroup ag = accountsGroupRepository.findByAccountAndGroup(account, group);

    // Check for the access right required for the current attempted operation.
    handlePermissions.accept(ag);
  }
  
}
