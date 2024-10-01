package com.project.service.mapper;

import com.project.domain.Customer;
import com.project.domain.District;
import com.project.domain.Province;
import com.project.domain.Ward;
import com.project.service.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CreateCustomerMapper extends EntityMapper<CreateCustomerDTO, Customer> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    @Mapping(target = "ward", source = "ward", qualifiedByName = "wardId")
    CreateCustomerDTO toDto(Customer s);

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
