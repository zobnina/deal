package ru.neoflex.learning.creaditpipeline.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.learning.creaditpipeline.deal.api.DealApi;
import ru.neoflex.learning.creaditpipeline.deal.model.FinishRegistrationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.deal.service.DealService;

import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class DealController implements DealApi {

    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> dealApplication(LoanApplicationRequestDto loanApplicationRequestDto) {
        return ResponseEntity.ok(dealService.application(loanApplicationRequestDto));
    }

    @Override
    public ResponseEntity<Void> dealCalculateApplicationId(BigDecimal applicationId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        dealService.calculate(applicationId, finishRegistrationRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> dealOffer(LoanOfferDto loanOfferDto) {
        dealService.offer(loanOfferDto);
        return ResponseEntity.noContent().build();
    }
}
