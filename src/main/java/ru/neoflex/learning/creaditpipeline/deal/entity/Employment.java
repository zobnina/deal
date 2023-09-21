package ru.neoflex.learning.creaditpipeline.deal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.learning.creaditpipeline.deal.model.EmploymentStatus;
import ru.neoflex.learning.creaditpipeline.deal.model.Position;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employment implements Serializable {

    @JsonProperty(value = "employment_status")
    private EmploymentStatus employmentStatus;

    @JsonProperty(value = "employer_inn")
    private String employerInn;

    private BigDecimal salary;

    private Position position;

    @JsonProperty(value = "work_experience_total")
    private Integer workExperienceTotal;

    @JsonProperty(value = "work_experience_current")
    private Integer workExperienceCurrent;
}
