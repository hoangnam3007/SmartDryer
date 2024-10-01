package com.project.service.mapper;

import com.project.domain.SendSMS;
import com.project.service.dto.SendSMSDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SendSMS} and its DTO {@link SendSMSDTO}.
 */
@Mapper(componentModel = "spring")
public interface SendSMSMapper extends EntityMapper<SendSMSDTO, SendSMS> {}
