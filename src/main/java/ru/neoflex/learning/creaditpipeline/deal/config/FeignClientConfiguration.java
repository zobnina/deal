package ru.neoflex.learning.creaditpipeline.deal.config;

import feign.Feign;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Feign feign() {

        return Feign.builder()
                .contract(new SpringMvcContract())
                .build();
    }
}