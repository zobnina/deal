package ru.neoflex.learning.creaditpipeline.deal.integration;

import org.springframework.cloud.openfeign.FeignClient;
import ru.neoflex.learning.creaditpipeline.deal.api.ConveyorApi;
import ru.neoflex.learning.creaditpipeline.deal.config.FeignClientConfiguration;

@FeignClient(value = "conveyorClient", url = "${application.url.conveyor}", configuration = FeignClientConfiguration.class)
public interface ConveyorFeignClient extends ConveyorApi {
}

