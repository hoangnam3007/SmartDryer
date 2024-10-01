package com.project.service.mapper;

import com.project.domain.HistoryOrder;
import com.project.domain.Order;
import com.project.service.dto.HistoryOrderDTO;
import com.project.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoryOrder} and its DTO {@link HistoryOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoryOrderMapper extends EntityMapper<HistoryOrderDTO, HistoryOrder> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    HistoryOrderDTO toDto(HistoryOrder s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
