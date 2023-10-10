package ru.neoflex.learning.creaditpipeline.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.Theme;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.model.EmailMessage;

@Service
@RequiredArgsConstructor
public class DossierProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void send(Application application, Theme theme, String topic) {
        final EmailMessage message = getEmailMessage(application, theme);
        kafkaTemplate.send(topic, message);
    }

    private EmailMessage getEmailMessage(Application application, Theme theme) {
        return EmailMessage.builder()
            .applicationId(application.getId())
            .address(application.getClient().getEmail())
            .theme(theme)
            .build();
    }
}
