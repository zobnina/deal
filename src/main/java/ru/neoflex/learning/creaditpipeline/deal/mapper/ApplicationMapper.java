package ru.neoflex.learning.creaditpipeline.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    default Application createApplication(Client client) {
        return Application.builder()
            .client(client)
            .creationDate(LocalDate.now())
            .build();
    }
}
