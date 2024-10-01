package com.project.service.mapper;

import com.project.domain.District;
import com.project.domain.Province;
import com.project.service.dto.DistrictDTO;
import com.project.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link District} and its DTO {@link DistrictDTO}.
 */
@Mapper(componentModel = "spring")
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    DistrictDTO toDto(District s);

    @Named("provinceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoProvinceId(Province province);
}
