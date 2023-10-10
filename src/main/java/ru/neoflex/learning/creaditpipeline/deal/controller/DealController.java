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
import ru.neoflex.learning.creaditpipeline.deal.service.DocumentService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class DealController implements DealApi {

    private final DealService dealService;
    private final DocumentService documentService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> dealApplication(LoanApplicationRequestDto loanApplicationRequestDto) {
        return ResponseEntity.ok(dealService.application(loanApplicationRequestDto));
    }

    @Override
    public ResponseEntity<Void> dealCalculateApplicationId(Long applicationId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        dealService.calculate(applicationId, finishRegistrationRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> dealDocumentApplicationIdCode(Long applicationId) {
        documentService.code(applicationId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> dealDocumentApplicationIdSend(Long applicationId) {
        documentService.send(applicationId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> dealDocumentApplicationIdSign(Long applicationId) {
        documentService.sign(applicationId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> dealOffer(LoanOfferDto loanOfferDto) {
        dealService.offer(loanOfferDto);
        return ResponseEntity.noContent().build();
    }
}
