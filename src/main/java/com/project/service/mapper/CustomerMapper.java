package com.project.service.mapper;

import com.project.domain.Customer;
import com.project.domain.District;
import com.project.domain.Province;
import com.project.domain.Ward;
import com.project.service.dto.CustomerDTO;
import com.project.service.dto.DistrictDTO;
import com.project.service.dto.ProvinceDTO;
import com.project.service.dto.WardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    @Mapping(target = "ward", source = "ward", qualifiedByName = "wardId")
    CustomerDTO toDto(Customer s);

    @Named("provinceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoProvinceId(Province province);

    @Named("districtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DistrictDTO toDtoDistrictId(District district);

    @Named("wardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WardDTO toDtoWardId(Ward ward);
}
