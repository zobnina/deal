package ru.neoflex.learning.creaditpipeline.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.learning.creaditpipeline.deal.model.FinishRegistrationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.ScoringDataDto;

@Mapper(componentModel = "spring")
public interface ConveyorMapper {
    //ToDo
    ScoringDataDto toScoringDataDto(FinishRegistrationRequestDto finishRegistrationRequestDto);
}
