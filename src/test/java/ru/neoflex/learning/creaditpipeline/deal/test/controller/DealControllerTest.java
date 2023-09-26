package ru.neoflex.learning.creaditpipeline.deal.test.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import ru.neoflex.learning.creaditpipeline.deal.entity.Client;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureWireMock
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = TestConfig.class)
class DealControllerTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
    private final Faker faker = new Faker();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApplicationRepository applicationRepository;

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
}
