package ru.neoflex.learning.creaditpipeline.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.entity.ApplicationStatusHistory;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
import ru.neoflex.learning.creaditpipeline.deal.entity.LoanOffer;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanOfferDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    default Application createApplication(Client client) {
        return Application.builder()
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDate.now())
            .build();
    }

    default ApplicationStatusHistory toApplicationStatusHistory(Application application) {
        return ApplicationStatusHistory.builder()
            .status(application.getStatus())
            .time(LocalDateTime.now())
            .build();
    }

    LoanOffer toLoanOffer(LoanOfferDto loanOfferDto);
}
