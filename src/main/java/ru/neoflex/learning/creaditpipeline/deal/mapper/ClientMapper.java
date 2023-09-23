package ru.neoflex.learning.creaditpipeline.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanApplicationRequestDto;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "passport.series", source = "passportSeries")
    @Mapping(target = "passport.number", source = "passportNumber")
    Client toClient(LoanApplicationRequestDto loanApplicationRequestDto);
}
