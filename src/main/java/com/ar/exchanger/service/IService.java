package com.ar.exchanger.service;


import com.ar.exchanger.entity.Currency;
import com.ar.exchanger.models.CurrencyPostDTO;

import java.io.InputStreamReader;
import java.util.List;

public interface IService {

    List<Currency> getAllCurrencies();

    InputStreamReader createRequest(String code);

    void computeTotalAmount(List<Currency> currencies, CurrencyPostDTO dto);
}
