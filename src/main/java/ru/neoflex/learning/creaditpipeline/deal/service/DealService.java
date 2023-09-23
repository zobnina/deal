package ru.neoflex.learning.creaditpipeline.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
import ru.neoflex.learning.creaditpipeline.deal.integration.ConveyorFeignClient;
import ru.neoflex.learning.creaditpipeline.deal.mapper.ApplicationMapper;
import ru.neoflex.learning.creaditpipeline.deal.mapper.ClientMapper;
import ru.neoflex.learning.creaditpipeline.deal.model.FinishRegistrationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.deal.repository.ApplicationRepository;
import ru.neoflex.learning.creaditpipeline.deal.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ApplicationMapper applicationMapper;
    private final ClientMapper clientMapper;
    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
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

    public void calculate(BigDecimal applicationId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        //ToDo
    }

    public void offer(LoanOfferDto loanOfferDto) {
        //ToDo
    }
}
