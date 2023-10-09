package ru.neoflex.learning.creaditpipeline.deal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JsonMapperConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        return JsonMapper.builder()
            .findAndAddModules()
            .build();
    }

    @Bean
    HibernatePropertiesCustomizer jsonFormatMapperCustomizer(ObjectMapper objectMapper) {
        return properties -> properties.put(AvailableSettings.JSON_FORMAT_MAPPER, new JacksonJsonFormatMapper(objectMapper));
    }
}
