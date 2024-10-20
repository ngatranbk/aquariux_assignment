package com.aquariux.cryptotrading.controller;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.dto.*;
import com.aquariux.cryptotrading.service.PriceAggregationService;
import com.aquariux.cryptotrading.service.TradingService;
import com.aquariux.cryptotrading.service.UserService;
import com.aquariux.cryptotrading.validator.CryptoTradingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
public class MarketController {
  @Autowired private PriceAggregationService priceService;

  @Autowired private TradingService tradingService;

  @Autowired private UserService userService;

  @GetMapping("/price/{cryptoSymbol}")
  public ResponseEntity<CryptoTradingResponseDto> getLatestPrice(
      @PathVariable String cryptoSymbol) {
    CryptoTradingResponseDto response = new CryptoTradingResponseDto();
    if (!CryptoTradingValidator.isCryptoSymbolValid(cryptoSymbol)) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Invalid crypto symbol");
      response.setError(true);
      return ResponseEntity.badRequest().body(response);
    }
    MarketPriceDto latestPrice = priceService.getLatestPrice(CryptoSymbolEnum.from(cryptoSymbol));
    if (latestPrice == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      response.setMessage("Market price not found");
      response.setError(true);
      return ResponseEntity.ok(response);
    }
    response.setStatus(HttpStatus.OK.value());
    response.setData(latestPrice);
    return ResponseEntity.ok(response);
  }
}
