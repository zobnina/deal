package ru.neoflex.learning.creaditpipeline.deal.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.Theme;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    String address;

    Theme theme;

    Long applicationId;
}
