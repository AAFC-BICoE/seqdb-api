package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.NumberLetterMappingUtils;
import ca.gc.aafc.seqdb.api.dto.LocationDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;
import ca.gc.aafc.seqdb.entities.Container;
import ca.gc.aafc.seqdb.entities.ContainerType;

@Component
public class LocationRepository extends JpaResourceRepository<LocationDto> {

  public LocationRepository(JpaDtoRepository dtoRepository, SimpleFilterHandler simpleFilterHandler,
      RsqlFilterHandler rsqlFilterHandler, ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(LocationDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler,
            groupFilterFactory.create(root -> root.get("container").get("group"))),
        metaInformationProvider);
  }

  @Override
  public <S extends LocationDto> S save(S resource) {
    S location = super.save(resource);
    this.validateCoordinates(location);
    return location;
  }

  @Override
  public <S extends LocationDto> S create(S resource) {
    S location = super.create(resource);
    this.validateCoordinates(location);
    return location;
  }
  
  private void validateCoordinates(LocationDto location) {
    EntityManager em = this.dtoRepository.getEntityManager();

    Container container = em.find(
        Container.class,
        location.getContainer().getContainerId()
    );

    ContainerType cType = container.getContainerType();

    Integer rows = cType.getNumberOfRows();
    Integer cols = cType.getNumberOfColumns();

    String row = location.getWellRow();
    Integer col = location.getWellColumn();

    // Checks that the coordinates match the integer followed by letter pattern
    if (!Pattern.matches("[a-zA-Z]*", row)) {
      throw new ValidationException("Well row must be in a letter format. (e.g: D)");
    }

    // Checks that the well column number is not 0
    if (col <= 0) {
      throw new ValidationException(String.format("Well column %s is less than 1.", col));
    }

    if (col > cols) {
      throw new ValidationException(
          String.format("Well column %s exceeds container's number of columns.", col)
      );
    }

    if (NumberLetterMappingUtils.getNumber(row) > rows) {
      throw new ValidationException(
          String.format("Row letter %s exceeds container's number of rows.", row)
      );
    }
  }

}
