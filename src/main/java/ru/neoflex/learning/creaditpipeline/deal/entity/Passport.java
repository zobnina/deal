package ru.neoflex.learning.creaditpipeline.deal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passport implements Serializable {

    private String series;

    private String number;

    @JsonProperty(value = "issue_date")
    private LocalDate issueDate;

    @JsonProperty(value = "issue_branch")
    private String issueBranch;
}
