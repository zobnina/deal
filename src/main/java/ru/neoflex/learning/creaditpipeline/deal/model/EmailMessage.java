package ru.neoflex.learning.creaditpipeline.deal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.Theme;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    private String address;

    private Theme theme;

    private Long applicationId;
}
