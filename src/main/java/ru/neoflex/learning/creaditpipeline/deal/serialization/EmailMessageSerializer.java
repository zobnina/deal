package ru.neoflex.learning.creaditpipeline.deal.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import ru.neoflex.learning.creaditpipeline.deal.model.EmailMessage;

@Slf4j
public class EmailMessageSerializer implements Serializer<EmailMessage> {

    private final JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();

    @Override
    public byte[] serialize(String topic, EmailMessage data) {
        try {
            return jsonMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error("serialize() - error: ", e);
            return null;
        }
    }
}
