package com.aquariux.cryptotrading.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BinancePriceResponse {
  List<BinancePriceElement> priceList;
}
