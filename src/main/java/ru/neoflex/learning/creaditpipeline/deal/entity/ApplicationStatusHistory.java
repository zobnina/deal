package ru.neoflex.learning.creaditpipeline.deal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ChangeType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistory implements Serializable {

    private ApplicationStatus status;

    private LocalDateTime time;

    @JsonProperty(value = "change_type")
    private ChangeType changeType;
}
