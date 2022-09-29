package com.example.learncrypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    String symbol;
    String name;
    Double price;
    Double percent_change_1h;
    Double percent_change_24h;
    Double percent_change_7d;
    String circulating_supply;
}
