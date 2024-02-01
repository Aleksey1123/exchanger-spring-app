package com.ar.exchanger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String code;
    private Double rate;
}
