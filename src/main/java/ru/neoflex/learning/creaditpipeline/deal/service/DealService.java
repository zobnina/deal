package ru.neoflex.learning.creaditpipeline.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
import ru.neoflex.learning.creaditpipeline.deal.entity.Credit;
import ru.neoflex.learning.creaditpipeline.deal.integration.ConveyorFeignClient;
import ru.neoflex.learning.creaditpipeline.deal.mapper.ApplicationMapper;
import ru.neoflex.learning.creaditpipeline.deal.mapper.ClientMapper;
import ru.neoflex.learning.creaditpipeline.deal.mapper.ConveyorMapper;
import ru.neoflex.learning.creaditpipeline.deal.mapper.CreditMapper;
import ru.neoflex.learning.creaditpipeline.deal.model.CreditDto;
import ru.neoflex.learning.creaditpipeline.deal.model.FinishRegistrationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.deal.model.ScoringDataDto;
import ru.neoflex.learning.creaditpipeline.deal.repository.ApplicationRepository;
import ru.neoflex.learning.creaditpipeline.deal.repository.ClientRepository;
import ru.neoflex.learning.creaditpipeline.deal.repository.CreditRepository;

import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ApplicationMapper applicationMapper;
    private final ClientMapper clientMapper;
    private final ConveyorMapper conveyorMapper;
    private final CreditMapper creditMapper;

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    private final ConveyorFeignClient conveyorFeignClient;

    public List<LoanOfferDto> application(LoanApplicationRequestDto loanApplicationRequestDto) {
        final Client client = clientRepository.save(clientMapper.toClient(loanApplicationRequestDto));
        log.info("application() - saved client = {}", client); //ToDo masking
        final Application application = applicationRepository.save(applicationMapper.createApplication(client));
        log.info("application() - saved application = {}", application);
        final List<LoanOfferDto> conveyorResponse = conveyorFeignClient.conveyorOffers(loanApplicationRequestDto).getBody();
        ofNullable(conveyorResponse).ifPresent(c -> c.forEach(loanOfferDto -> loanOfferDto.setApplicationId(application.getId())));
        return conveyorResponse;
    }

    public void calculate(Long applicationId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        final Application application = applicationRepository.findById(applicationId)
            .orElseThrow(EntityNotFoundException::new);
        final ScoringDataDto scoringDataDto = conveyorMapper.toScoringDataDto(finishRegistrationRequestDto);
        final CreditDto creditDto = conveyorFeignClient.conveyorCalculation(scoringDataDto).getBody();
        final Credit credit = creditRepository.save(creditMapper.toCredit(creditDto));
        application.setCredit(credit);
        updateStatus(application, ApplicationStatus.CC_APPROVED);
        applicationRepository.save(application);
    }

    public void offer(LoanOfferDto loanOfferDto) {
        final Application application = applicationRepository.findById(loanOfferDto.getApplicationId())
            .orElseThrow(EntityNotFoundException::new);
        updateStatus(application, ApplicationStatus.APPROVED);
        application.setAppliedOffer(applicationMapper.toLoanOffer(loanOfferDto));
        applicationRepository.save(application);
    }

    private void updateStatus(Application application, ApplicationStatus status) {
        application.getStatusHistory().getStatusHistory().add(applicationMapper.toApplicationStatusHistory(application));
        application.setStatus(status);
    }
}