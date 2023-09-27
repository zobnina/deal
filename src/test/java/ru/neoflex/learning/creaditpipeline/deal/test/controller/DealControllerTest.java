package ru.neoflex.learning.creaditpipeline.deal.test.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;
import ru.neoflex.learning.creaditpipeline.deal.entity.Application;
import ru.neoflex.learning.creaditpipeline.deal.entity.ApplicationStatusHistory;
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
import ru.neoflex.learning.creaditpipeline.deal.entity.LoanOffer;
import ru.neoflex.learning.creaditpipeline.deal.entity.Passport;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.deal.model.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.deal.repository.ApplicationRepository;
import ru.neoflex.learning.creaditpipeline.deal.test.TestConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureWireMock
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = TestConfig.class)
class DealControllerTest {

    private final Faker faker = new Faker();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @Test
    @DisplayName("POST /deal/application")
    @Transactional
    void applicationTest() throws Exception {

        final LoanApplicationRequestDto request = getLoanApplicationRequestDto();
        final LoanOfferDto loanOfferDto = getLoanOfferDto(request);
        final List<LoanOfferDto> loanOfferDtos = List.of(loanOfferDto);

        stubFor(WireMock.post(urlPathEqualTo("/conveyor/offers"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(jsonMapper.writeValueAsString(loanOfferDtos))));

        final String responseJson = mockMvc.perform(post("/deal/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsBytes(request)))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        final List<LoanOfferDto> response = jsonMapper.readValue(responseJson, new TypeReference<>() {
        });

        assertEquals(loanOfferDtos.size(), response.size());
        final LoanOfferDto responseItem = response.get(0);
        assertNotNull(responseItem.getApplicationId());
        assertEquals(loanOfferDto.getRequestedAmount(), responseItem.getRequestedAmount());
        assertEquals(loanOfferDto.getTotalAmount(), responseItem.getTotalAmount());
        assertEquals(loanOfferDto.getTerm(), responseItem.getTerm());
        assertEquals(loanOfferDto.getMonthlyPayment(), responseItem.getMonthlyPayment());
        assertEquals(loanOfferDto.getRate(), responseItem.getRate());
        assertEquals(loanOfferDto.getIsInsuranceEnabled(), responseItem.getIsInsuranceEnabled());
        assertEquals(loanOfferDto.getIsSalaryClient(), responseItem.getIsSalaryClient());

        final Optional<Application> optional = applicationRepository.findById(responseItem.getApplicationId());
        assertTrue(optional.isPresent());
        final Application application = optional.get();
        assertEquals(ApplicationStatus.PREAPPROVAL, application.getStatus());
        assertEquals(LocalDate.now(), application.getCreationDate());
        final Client client = application.getClient();
        assertEquals(request.getFirstName(), client.getFirstName());
        assertEquals(request.getLastName(), client.getLastName());
        assertEquals(request.getBirthdate(), client.getBirthDate());
        assertEquals(request.getEmail(), client.getEmail());
        assertEquals(request.getPassportSeries(), client.getPassport().getSeries());
        assertEquals(request.getPassportNumber(), client.getPassport().getNumber());
    }

    @Test
    @DisplayName("PUT /deal/offer 404")
    void offer404Test() throws Exception {

        final LoanOfferDto request = getLoanOfferDto();

        mockMvc.perform(put("/deal/offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsBytes(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /deal/offer")
    void offerTest() throws Exception {

        final Application application = applicationRepository.save(getApplication());
        final LoanOfferDto request = getLoanOfferDto().applicationId(application.getId());

        mockMvc.perform(put("/deal/offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsBytes(request)))
            .andExpect(status().isNoContent());

        final Optional<Application> optional = applicationRepository.findById(application.getId());
        assertTrue(optional.isPresent());
        final Application fromDb = optional.get();
        assertEquals(ApplicationStatus.APPROVED, fromDb.getStatus());
        assertEquals(1, fromDb.getStatusHistory().getStatusHistory().size());
        final ApplicationStatusHistory applicationStatusHistory = fromDb.getStatusHistory().getStatusHistory().get(0);
        assertEquals(ApplicationStatus.PREAPPROVAL, applicationStatusHistory.getStatus());
        assertEquals(LocalDate.now(), applicationStatusHistory.getTime().toLocalDate());
        final LoanOffer appliedOffer = fromDb.getAppliedOffer();
        assertNotNull(appliedOffer);
        assertEquals(request.getRequestedAmount(), appliedOffer.getRequestedAmount());
        assertEquals(request.getTotalAmount(), appliedOffer.getTotalAmount());
        assertEquals(request.getTerm(), appliedOffer.getTerm());
        assertEquals(request.getMonthlyPayment(), appliedOffer.getMonthlyPayment());
        assertEquals(request.getRate(), appliedOffer.getRate());
        assertEquals(request.getIsInsuranceEnabled(), appliedOffer.getIsInsuranceEnabled());
        assertEquals(request.getIsSalaryClient(), appliedOffer.getIsSalaryClient());
    }

    private Application getApplication() {
        return Application.builder()
            .client(getClient())
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDate.now())
            .build();
    }

    private Client getClient() {
        return Client.builder()
            .lastName(faker.name().lastName())
            .firstName(faker.name().firstName())
            .email(faker.internet().emailAddress())
            .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .passport(Passport.builder()
                .series(faker.number().digits(4))
                .number(faker.number().digits(6))
                .build())
            .build();
    }

    private LoanApplicationRequestDto getLoanApplicationRequestDto() {
        return LoanApplicationRequestDto.builder()
            .amount(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .term(faker.number().numberBetween(6, 360))
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .email(faker.internet().emailAddress())
            .birthdate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .passportSeries(faker.number().digits(4))
            .passportNumber(faker.number().digits(6))
            .build();
    }

    private LoanOfferDto getLoanOfferDto(LoanApplicationRequestDto loanApplicationRequestDto) {
        return LoanOfferDto.builder()
            .requestedAmount(loanApplicationRequestDto.getAmount())
            .totalAmount(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .term(loanApplicationRequestDto.getTerm())
            .monthlyPayment(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .rate(BigDecimal.valueOf(faker.number().numberBetween(5, 15)))
            .isInsuranceEnabled(faker.bool().bool())
            .isSalaryClient(faker.bool().bool())
            .build();
    }

    private LoanOfferDto getLoanOfferDto() {
        return LoanOfferDto.builder()
            .applicationId(faker.number().randomNumber())
            .requestedAmount(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .totalAmount(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .term(faker.number().numberBetween(6, 360))
            .monthlyPayment(BigDecimal.valueOf(faker.number().numberBetween(10000, 1000000)))
            .rate(BigDecimal.valueOf(faker.number().numberBetween(5, 15)))
            .isInsuranceEnabled(faker.bool().bool())
            .isSalaryClient(faker.bool().bool())
            .build();
    }
}
