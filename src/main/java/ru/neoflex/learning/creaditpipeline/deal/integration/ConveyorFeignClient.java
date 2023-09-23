package ru.neoflex.learning.creaditpipeline.deal.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import ru.neoflex.learning.creaditpipeline.deal.api.ConveyorApi;

@FeignClient(value = "conveyorClient",
    url = "${application.url.conveyor}",
    configuration = FeignClientProperties.FeignClientConfiguration.class)
public class ConveyorFeignClient implements ConveyorApi {

}
