package ca.gc.aafc.seqdb.api.security.authorization;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

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
import io.crnk.core.exception.ForbiddenException;
import io.crnk.core.exception.UnauthorizedException;
import io.crnk.core.queryspec.QuerySpec;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Intercepts calls to the "findOne", "create", "save" and "delete" methods of JpaResourceRepository
 * to apply authorization permission checks.
 */
@Named
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Inject, access = AccessLevel.PACKAGE)
@Aspect
public class GroupAuthorizationAspect {

  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final AccountRepository accountRepository;
  
  @NonNull
  private final AccountsGroupRepository accountsGroupRepository;
  
  @NonNull
  private final JpaDtoMapper jpaDtoMapper;
  
  /**
   * Intercepts the findOne operation to apply Group-based authorization.
   * 
   * @param joinPoint
   *          Provides reflective access to both the state available at a join point and static
   *          information about it.
   * @param result
   *          The result of the findOne operation.
   */
  @AfterReturning(
      pointcut = "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.findOne(..))",
      returning = "result"
  )
  public void findOneInterceptor(JoinPoint joinPoint, Object result) {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    this.requireGroupAccess(
        result,
        "Read",
        AccountsGroup::hasReadAccess,
        repository.getResourceRegistry()
    );
  }
  
  /**
   * Intercepts the create operation to apply Group-based authorization.
   * 
   * @param joinPoint
   *          Provides reflective access to both the state available at a join point and static
   *          information about it.
   * @param result
   *          The result of the create operation.
   */
  @AfterReturning(
      pointcut = "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.create(..))",
      returning = "result"
  )
  public void createInterceptor(JoinPoint joinPoint, Object result) {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    this.requireGroupAccess(
        result,
        "Create",
        AccountsGroup::hasCreateAccess,
        repository.getResourceRegistry()
    );
  }
  
  /**
   * Intercepts the "save" operation to apply Group-based authorization.
   * 
   * @param joinPoint
   *          Provides reflective access to both the state available at a join point and static
   *          information about it.
   * @param inputDto
   *          The input DTO to be saved.
   * @return The saved DTO.
   * @throws Throwable
   */
  @Around(
      "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.save(..))"
      + " && args(inputDto)"
  )
  public Object saveInterceptor(ProceedingJoinPoint joinPoint, Object inputDto) throws Throwable {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    // Require group access before the edit.
    this.requireGroupAccess(
        inputDto,
        "Write",
        AccountsGroup::hasWriteAccess,
        repository.getResourceRegistry()
    );
    
    // Call the save method
    Object resultDto = joinPoint.proceed(joinPoint.getArgs());
    
    // Require group access after the edit, in case the object's group was changed.
    this.requireGroupAccess(
        resultDto,
        "Write",
        AccountsGroup::hasWriteAccess,
        repository.getResourceRegistry()
    );
    
    // Return the DTO that the intercepted save method returned.
    return resultDto;
  }
  
  /**
   * Intercepts the "save" operation to apply Group-based authorization.
   * 
   * @param joinPoint
   *          Provides reflective access to both the state available at a join point and static
   *          information about it.
   * @param id
   *          The id of the resource to delete.
   */
  @Before(
      "execution(* ca.gc.aafc.seqdb.api.repository.JpaResourceRepository+.delete(..))"
      + " && args(id)"
  )
  public void deleteInterceptor(JoinPoint joinPoint, Serializable id) {
    JpaResourceRepository<?> repository = (JpaResourceRepository<?>) joinPoint.getThis();
    
    this.requireGroupAccess(
        repository.findOne(id, new QuerySpec(repository.getResourceClass())),
        "Delete",
        AccountsGroup::hasDeleteAccess,
        repository.getResourceRegistry()
    );
  }
  
  /**
   * Throws an exception if the current user is not authorized.
   * 
   * @param dto
   *          The dto to require Group access on.
   * @param operationName
   *          The name of the CRUD operation.
   * @param permissionChecker
   *          The accountsgroup method that checks permission for this operation.
   * @param resourceRegistry
   *          Crnk's ResourceRegistry
   */
  private void requireGroupAccess(
      Object dto,
      String operationName,
      Function<AccountsGroup, Boolean> permissionChecker,
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

    // Check the user's permission and throw the ForbiddenException if the user does not have the
    // required permission.
    if (Optional.ofNullable(ag).map(it -> !permissionChecker.apply(it)).orElse(true)) {
      throw new ForbiddenException(
          operationName + " access denied to " + restrictedObject.getClass().getSimpleName()
              + " belonging to Group " + group.getGroupName()
      );
    }
  }
  
}
