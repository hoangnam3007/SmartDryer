package com.project.service.mapper;

import com.project.domain.Customer;
import com.project.domain.District;
import com.project.domain.Order;
import com.project.domain.Province;
import com.project.domain.Sale;
import com.project.domain.SourceOrder;
import com.project.domain.Staff;
import com.project.domain.Ward;
import com.project.service.dto.CustomerDTO;
import com.project.service.dto.DistrictDTO;
import com.project.service.dto.OrderDTO;
import com.project.service.dto.ProvinceDTO;
import com.project.service.dto.SaleDTO;
import com.project.service.dto.SourceOrderDTO;
import com.project.service.dto.StaffDTO;
import com.project.service.dto.WardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "sale", source = "sale", qualifiedByName = "saleId")
    @Mapping(target = "staff", source = "staff", qualifiedByName = "staffId")
    @Mapping(target = "sourceOrder", source = "sourceOrder", qualifiedByName = "sourceOrderId")
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    @Mapping(target = "district", source = "district", qualifiedByName = "districtId")
    @Mapping(target = "ward", source = "ward", qualifiedByName = "wardId")
    OrderDTO toDto(Order s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("saleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SaleDTO toDtoSaleId(Sale sale);

    @Named("staffId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StaffDTO toDtoStaffId(Staff staff);

    @Named("sourceOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceOrderDTO toDtoSourceOrderId(SourceOrder sourceOrder);

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
