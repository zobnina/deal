package ru.neoflex.learning.creaditpipeline.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.Theme;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.properties.DossierTopicProperties;
import ru.neoflex.learning.creaditpipeline.deal.repository.ApplicationRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ApplicationRepository applicationRepository;
    private final DossierTopicProperties dossierTopicProperties;
    private final DossierProducer dossierProducer;

    public void code(Long applicationId) {
        final Application application = applicationRepository.getByIdOrThrow(applicationId);
        applicationRepository.save(application.setStatus(ApplicationStatus.DOCUMENT_SIGNED).setSignDate(LocalDate.now()));
        dossierProducer.send(application, Theme.CREDIT_ISSUED, dossierTopicProperties.getCreditIssued());
    }

    public void send(Long applicationId) {
        final Application application = applicationRepository.getByIdOrThrow(applicationId);
        applicationRepository.save(application.setStatus(ApplicationStatus.PREPARE_DOCUMENTS));
        dossierProducer.send(application, Theme.SEND_DOCUMENTS, dossierTopicProperties.getSendDocuments());
    }

    public void sign(Long applicationId) {
        final Application application = applicationRepository.getByIdOrThrow(applicationId);
        applicationRepository.save(application.setSesCode(UUID.randomUUID().toString()));
        dossierProducer.send(application, Theme.SEND_SES, dossierTopicProperties.getSendSes());
    }
}
