package ru.neoflex.learning.creaditpipeline.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.learning.creaditpipeline.deal.entity.Credit;
import ru.neoflex.learning.creaditpipeline.deal.model.CreditDto;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "paymentSchedule.paymentScheduleList", source = "paymentSchedule")
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto creditDto);
}
