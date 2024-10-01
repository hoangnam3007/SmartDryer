package com.project.service.mapper;

import com.project.domain.Staff;
import com.project.service.dto.StaffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Staff} and its DTO {@link StaffDTO}.
 */
@Mapper(componentModel = "spring")
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {
    @Mapping(target = "staffLead", source = "staffLead", qualifiedByName = "staffId")
    StaffDTO toDto(Staff s);

    @Named("staffId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StaffDTO toDtoStaffId(Staff staff);
}
