package com.ar.exchanger.service;


import com.ar.exchanger.entity.Currency;
import com.ar.exchanger.models.CurrencyPostDTO;
import com.ar.exchanger.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceImpl implements IService {

    public final static String API_KEY = "86f86e14f949a172278ea719";
    private final CurrencyRepository repository;

    public String buildUrl(String code) {

        return String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", API_KEY, code);
    }

    public InputStreamReader createRequest(String code) {
        try {
            URL url = URI.create(buildUrl(code)).toURL();
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            return new InputStreamReader((InputStream) request.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return repository.findAll();
    }

    @Override
    public void computeTotalAmount(List<Currency> currencies, CurrencyPostDTO dto) {

        double currencyFromVal = 0;
        double currencyToVal = 0;
        for (var currency : currencies) {
            if (dto.getCurrencyFrom().equals(currency.getCode()))
                currencyFromVal = currency.getRate();
            if (dto.getCurrencyTo().equals(currency.getCode()))
                currencyToVal = currency.getRate();
        }

        Double total = Double.parseDouble(dto.getAmount()) * (1/currencyFromVal) * currencyToVal;
        dto.setResult(String.format("%.3f", total));
    }
}
