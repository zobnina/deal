package ru.neoflex.learning.creaditpipeline.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule implements Serializable {

    List<PaymentScheduleElement> paymentSchedule;
}
