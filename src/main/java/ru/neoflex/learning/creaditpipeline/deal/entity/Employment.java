package ru.neoflex.learning.creaditpipeline.deal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.neoflex.learning.creaditpipeline.deal.model.EmploymentStatus;
import ru.neoflex.learning.creaditpipeline.deal.model.Position;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Employment implements Serializable {

    @JsonProperty(value = "employment_status")
    EmploymentStatus employmentStatus;

    @JsonProperty(value = "employer_inn")
    String employerInn;

    BigDecimal salary;

    Position position;

    @JsonProperty(value = "work_experience_total")
    Integer workExperienceTotal;

    @JsonProperty(value = "work_experience_current")
    Integer workExperienceCurrent;
}
