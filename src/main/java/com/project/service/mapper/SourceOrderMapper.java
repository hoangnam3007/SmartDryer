package com.project.service.mapper;

import com.project.domain.SourceOrder;
import com.project.service.dto.SourceOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SourceOrder} and its DTO {@link SourceOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface SourceOrderMapper extends EntityMapper<SourceOrderDTO, SourceOrder> {}
