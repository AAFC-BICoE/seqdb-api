package ca.gc.aafc.seqdb.api.mapper;

import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.entities.Product;

@Mapper
public interface ProductMapper extends DinaMapperV2<ProductDto, Product> {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  ProductDto toDto(Product entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  Product toEntity(ProductDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget Product entity, ProductDto dto,
                   @Context Set<String> provided, @Context String scope);
}

