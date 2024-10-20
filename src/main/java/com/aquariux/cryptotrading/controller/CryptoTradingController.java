package com.aquariux.cryptotrading.controller;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.constants.TradeErrorMessage;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;
import com.aquariux.cryptotrading.dto.CryptoTradingResponse;
import com.aquariux.cryptotrading.dto.MarketPriceDto;
import com.aquariux.cryptotrading.dto.TradeRequestDto;
import com.aquariux.cryptotrading.dto.WalletBalanceDto;
import com.aquariux.cryptotrading.service.PriceAggregationService;
import com.aquariux.cryptotrading.service.TradingService;
import com.aquariux.cryptotrading.service.UserService;
import com.aquariux.cryptotrading.validator.CryptoTradingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CryptoTradingController {
  @Autowired private PriceAggregationService priceService;

  @Autowired private TradingService tradingService;

  @Autowired private UserService userService;

  @GetMapping("/price/{cryptoSymbol}")
  public ResponseEntity<CryptoTradingResponse> getLatestPrice(@PathVariable String cryptoSymbol) {
    CryptoTradingResponse response = new CryptoTradingResponse();
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

  @PostMapping("/trade")
  public ResponseEntity<CryptoTradingResponse> tradeCrypto(
      @RequestBody TradeRequestDto tradeRequest) {
    CryptoTradingResponse response = new CryptoTradingResponse();
    if (!CryptoTradingValidator.isCryptoSymbolValid(tradeRequest.getCryptoSymbol())) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Invalid crypto symbol");
      response.setError(true);
      return ResponseEntity.badRequest().body(response);
    }
    if (!CryptoTradingValidator.isTxnTypeValid(tradeRequest.getTxnType())) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Invalid transaction type");
      response.setError(true);
      return ResponseEntity.badRequest().body(response);
    }
    TradeErrorMessage result =
        tradingService.tradeCrypto(
            tradeRequest.getUserId(),
            CryptoSymbolEnum.from(tradeRequest.getCryptoSymbol()),
            TxnTypeEnum.from(tradeRequest.getTxnType()),
            tradeRequest.getAmount());
    if (TradeErrorMessage.OK.equals(result)) {
      response.setStatus(HttpStatus.OK.value());
      response.setError(false);
    } else {
      response.setStatus(HttpStatus.INSUFFICIENT_STORAGE.value());
      response.setError(true);
    }
    response.setMessage(result.getMessage());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/wallet/{userId}")
  public ResponseEntity<CryptoTradingResponse> getWalletBalance(@PathVariable Long userId) {
    CryptoTradingResponse response = new CryptoTradingResponse();
    WalletBalanceDto walletBalanceDto = userService.retrieveWalletBalance(userId);
    if (walletBalanceDto == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      response.setError(true);
    } else {
      response.setStatus(HttpStatus.OK.value());
      response.setData(walletBalanceDto);
      response.setError(false);
    }
    return ResponseEntity.ok(response);
  }
}
