package com.aquariux.cryptotrading.controller;

import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.constants.TradeErrorMessage;
import com.aquariux.cryptotrading.constants.TxnTypeEnum;
import com.aquariux.cryptotrading.dto.*;
import com.aquariux.cryptotrading.service.PriceAggregationService;
import com.aquariux.cryptotrading.service.TradingService;
import com.aquariux.cryptotrading.service.UserService;
import com.aquariux.cryptotrading.validator.CryptoTradingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired private PriceAggregationService priceService;

  @Autowired private TradingService tradingService;

  @Autowired private UserService userService;

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

  @GetMapping("/{userId}/wallet")
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

  @GetMapping("/{userId}/transactions")
  public ResponseEntity<CryptoTradingResponse> getTradeHistory(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size) {
    CryptoTradingResponse response = new CryptoTradingResponse();
    Page<TradeTransactionDto> transactionPage =
        tradingService.getTradeHistoryByUser(userId, page, size);
    if (transactionPage.isEmpty()) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      response.setMessage("Transactions not found");
      response.setError(true);
    } else {
      response.setStatus(HttpStatus.OK.value());
      response.setData(transactionPage);
      response.setError(false);
    }
    return ResponseEntity.ok(response);
  }
}
