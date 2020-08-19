package ca.gc.aafc.seqdb.api.auditing;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.AbstractEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.javers.core.Javers;
import org.javers.repository.jql.GlobalIdDTO;
import org.javers.repository.jql.InstanceIdDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.gc.aafc.dina.entity.SoftDeletable;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.mapper.JpaDtoMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.engine.internal.utils.PropertyUtils;
import lombok.RequiredArgsConstructor;

/**
 * Hooks into Hibernate to listen for create/update/delete events so it can
 * store audits of changed data.
 */
@Named
@RequiredArgsConstructor
@SuppressWarnings("serial")
@SuppressFBWarnings("SE_BAD_FIELD")
public class AuditListener implements PostUpdateEventListener, PostInsertEventListener, PreDeleteEventListener {

  private final Javers javers;
  private final EntityManagerFactory emf;
  private final BaseDAO baseDao;
  private final JpaDtoMapper jpaDtoMapper;
  private final SnapshotLoader snapshotLoader;

  /** Hook this listener into Hibernate's entity lifecycle methods. */
  @PostConstruct
  public void init() {
    EventListenerRegistry registry = emf.unwrap(SessionFactoryImpl.class).getServiceRegistry()
        .getService(EventListenerRegistry.class);

    registry.appendListeners(EventType.POST_INSERT, this);
    registry.appendListeners(EventType.POST_UPDATE, this);
    registry.appendListeners(EventType.PRE_DELETE, this);
  }

  @Override
  public boolean onPreDelete(PreDeleteEvent event) {
    persistSnapshot(event);
    return false;
  }

  @Override
  public void onPostInsert(PostInsertEvent event) {
    persistSnapshot(event);
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    persistSnapshot(event);
  }

  private void persistSnapshot(AbstractEvent event) {
    Object entity = PropertyUtils.getProperty(event, "entity");

    // Ignore non-audited entities where snapshot loader is not defined.
    if (!snapshotLoader.supports(entity.getClass())) {
      return;
    }

    // Replace this with the actual user name after we setup authentication:
    String author = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .orElse("anonymous")
        .toString();

    Class<?> eventType = event.getClass();

    if (eventType == PreDeleteEvent.class) {
      // If this is a delete, load the identifier because you can't look up a
      // snapshot:
      GlobalIdDTO globalId = loadGlobalId(entity);
      javers.commitShallowDeleteById(author, globalId);
      return;
    }

    Object snapshot = snapshotLoader.loadSnapshot(entity);

    boolean softDeleted = (entity instanceof SoftDeletable) && ((SoftDeletable) entity).getDeletedDate() != null;

    if (snapshot != null) {
      // Soft Deletes are treated as audit deletes:
      if (softDeleted) {
        javers.commitShallowDelete(author, snapshot);
      } else if (eventType == PostInsertEvent.class || eventType == PostUpdateEvent.class) {
        javers.commit(author, snapshot);
      }
    }
  }

  private GlobalIdDTO loadGlobalId(Object entity) {
    Object id = PropertyUtils.getProperty(entity, baseDao.getNaturalIdFieldName(entity.getClass()));
    Class<?> clazz = jpaDtoMapper.getDtoClassForEntity(entity.getClass());

    return InstanceIdDTO.instanceId(id, clazz);
  }

  @Override
  public boolean requiresPostCommitHanding(EntityPersister persister) {
    return false;
  }

}
