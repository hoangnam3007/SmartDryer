package com.project.service.mapper;

import com.project.domain.CusNote;
import com.project.domain.Customer;
import com.project.service.dto.CusNoteDTO;
import com.project.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CusNote} and its DTO {@link CusNoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CusNoteMapper extends EntityMapper<CusNoteDTO, CusNote> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    CusNoteDTO toDto(CusNote s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
