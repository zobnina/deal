package ru.neoflex.learning.creaditpipeline.deal.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.deal.dictionary.ApplicationStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusMetricService {

    public static final String COUNTER_STATUS_NAME = "application_status_count";
    public static final String DESCRIPTION = "Number of applications in each status";
    public static final String TAG_KEY = "status";

    private final MeterRegistry meterRegistry;
    Map<ApplicationStatus, Counter> counters;

    @PostConstruct
    void initStatusCounters() {
        counters = Arrays.stream(ApplicationStatus.values())
            .map(s -> Map.entry(s, Counter.builder(COUNTER_STATUS_NAME)
                .description(DESCRIPTION)
                .tag(TAG_KEY, s.name())
                .register(meterRegistry))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void incrementMetric(ApplicationStatus status) {
        counters.get(status).increment();
    }
}
