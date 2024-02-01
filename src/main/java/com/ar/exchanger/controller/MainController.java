package com.ar.exchanger.controller;

import com.ar.exchanger.entity.Currency;
import com.ar.exchanger.models.CurrencyPostDTO;
import com.ar.exchanger.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/exchanger-api")
@RequiredArgsConstructor
public class MainController {

    private final IService service;

    @ModelAttribute("currencies")
    public List<Currency> addAttributes() {
        return service.getAllCurrencies();
    }


    @GetMapping("/main")
    public String getMethod(Model model) {

        model.addAttribute("currencyDTO", new CurrencyPostDTO());
        return "main";
    }

    @PostMapping("/main")
    public String postMethod(Model model,
                             @ModelAttribute("currencyDTO") CurrencyPostDTO dto) {
        try {
            List<Currency> currencies = service.getAllCurrencies();
            service.computeTotalAmount(currencies, dto);

            model.addAttribute("currencyDTO", dto);
            return "main";
        }
        catch (NumberFormatException exception) {
            throw new RuntimeException("Not a number!");
        }
    }
}
