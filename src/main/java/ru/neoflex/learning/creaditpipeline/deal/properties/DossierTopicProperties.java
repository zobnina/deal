package ru.neoflex.learning.creaditpipeline.deal.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@ConfigurationProperties(prefix = "application.topic")
public class DossierTopicProperties {

    String finishRegistration;

    String createDocuments;

    String sendDocuments;

    String sendSes;

    String creditIssued;

    String applicationDenied;
}
