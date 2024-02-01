package com.ar.exchanger.bootstrap;

import com.ar.exchanger.entity.Currency;
import com.ar.exchanger.repository.CurrencyRepository;
import com.ar.exchanger.service.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CurrencyRepository repository;
    private final ServiceImpl serviceImpl;

    @Override
    public void run(String... args) throws Exception {
        loadCurrenciesList();
    }

    private void loadCurrenciesList() {

        if (repository.count() == 0) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode conversionRatesNode = objectMapper.readTree(serviceImpl.createRequest("USD"))
                        .get("conversion_rates");

                Map<String, Double> currencyMap = objectMapper.convertValue(conversionRatesNode, new TypeReference<>() {});
                List<Currency> currencies = new ArrayList<>();
                currencyMap.forEach((code, value) -> currencies.add(Currency.builder().code(code).rate(value).build()));

                repository.saveAll(currencies);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
