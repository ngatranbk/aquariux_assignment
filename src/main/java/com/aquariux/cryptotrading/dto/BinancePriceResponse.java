package com.aquariux.cryptotrading.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BinancePriceResponse {
    List<BinancePriceElement> priceList;
}
