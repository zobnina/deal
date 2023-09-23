package ru.neoflex.learning.creaditpipeline.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryList implements Serializable {

    @Builder.Default
    private List<ApplicationStatusHistory> statusHistory = new ArrayList<>();
}
