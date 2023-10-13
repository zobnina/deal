package ru.neoflex.learning.creaditpipeline.deal.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryList implements Serializable {

    @Builder.Default
    List<ApplicationStatusHistory> statusHistory = new ArrayList<>();
}
