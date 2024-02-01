package com.ar.exchanger.config;

import com.ar.exchanger.entity.Currency;
import com.ar.exchanger.repository.CurrencyRepository;
import com.ar.exchanger.service.IService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private final CurrencyRepository repository;
    private final IService service;

    @Scheduled(fixedRate = 120000)
    public void fetchDataFromExchangerAPI() {
        log.info("Прошло 2 минуты");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode conversionRatesNode = objectMapper.readTree(service.createRequest("USD"))
                    .get("conversion_rates");

            Map<String, Double> currencyMap = objectMapper.convertValue(conversionRatesNode, new TypeReference<>() {});
            List<Currency> currencyList = new ArrayList<>();
            currencyMap.forEach((currencyCode, currencyRate) -> currencyList.add(Currency.builder()
                    .code(currencyCode)
                    .rate(currencyRate)
                    .build()));
            repository.saveAll(currencyList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
