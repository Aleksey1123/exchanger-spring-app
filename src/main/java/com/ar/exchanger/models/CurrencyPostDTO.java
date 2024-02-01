package com.ar.exchanger.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyPostDTO {

    private String currencyFrom;
    private String amount;
    private String currencyTo;
    private String result;
}
