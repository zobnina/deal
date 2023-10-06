package ru.neoflex.learning.creaditpipeline.deal.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import ru.neoflex.learning.creaditpipeline.deal.DealApplication;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:13.1").asCompatibleSubstituteFor("postgres"))
            .waitingFor(Wait.forListeningPort())
            .withDatabaseName("deal")
            .withInitScript("init.sql");
    }

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
    }

    public static void main(String[] args) {
        SpringApplication.from(DealApplication::main).with(TestConfig.class).run(args);
    }
}
