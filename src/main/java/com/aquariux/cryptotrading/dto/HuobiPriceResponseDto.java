package com.aquariux.cryptotrading.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HuobiPriceResponseDto {
  private List<HuobiPriceElementDto> data;
  private String status;
}
