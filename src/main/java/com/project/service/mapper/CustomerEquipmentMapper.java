package com.project.service.mapper;

import com.project.domain.Customer;
import com.project.domain.CustomerEquipment;
import com.project.domain.Equipment;
import com.project.service.dto.CustomerDTO;
import com.project.service.dto.CustomerEquipmentDTO;
import com.project.service.dto.EquipmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerEquipment} and its DTO {@link CustomerEquipmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerEquipmentMapper extends EntityMapper<CustomerEquipmentDTO, CustomerEquipment> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "equipment", source = "equipment", qualifiedByName = "equipmentId")
    CustomerEquipmentDTO toDto(CustomerEquipment s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("equipmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EquipmentDTO toDtoEquipmentId(Equipment equipment);
}
