package ru.neoflex.learning.creaditpipeline.deal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ChangeType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistory implements Serializable {

    ApplicationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime time;

    @JsonProperty(value = "change_type")
    ChangeType changeType;
}
