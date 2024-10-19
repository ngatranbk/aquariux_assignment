package com.aquariux.cryptotrading.controller;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.dto.CryptoTradingResponse;
import com.aquariux.cryptotrading.dto.MarketPriceDto;
import com.aquariux.cryptotrading.service.PriceAggregationService;
import com.aquariux.cryptotrading.validator.CryptoTradingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CryptoTradingController {
  @Autowired private PriceAggregationService priceService;

  @GetMapping("/price/{cryptoSymbol}")
  public ResponseEntity<CryptoTradingResponse> getLatestPrice(@PathVariable String cryptoSymbol) {
    CryptoTradingResponse response = new CryptoTradingResponse();
    if (!CryptoTradingValidator.isCryptoSymbolValid(cryptoSymbol)) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Invalid crypto symbol");
      return ResponseEntity.badRequest().body(response);
    }
    MarketPriceDto latestPrice = priceService.getLatestPrice(CryptoSymbolEnum.from(cryptoSymbol));
    if (latestPrice == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      response.setMessage("Market price not found");
      return ResponseEntity.ok(response);
    }
    response.setStatus(HttpStatus.OK.value());
    response.setData(latestPrice);
    return ResponseEntity.ok(response);
  }
}
