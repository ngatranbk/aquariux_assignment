package com.aquariux.cryptotrading.service;

import com.aquariux.cryptotrading.constants.CryptoProvider;
import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.dto.*;
import com.aquariux.cryptotrading.model.MarketPrice;
import com.aquariux.cryptotrading.repository.MarketPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
public class PriceAggregationService {

    private static final Logger LOG = LoggerFactory.getLogger(PriceAggregationService.class);

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Value("${task.executor.maxPoolSize}")
    private int maxPoolSize;

    // Fetch data every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void fetchAndStoreBestPrices() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(CryptoProvider.values().length, maxPoolSize));

        CompletableFuture<BinancePriceResponse> binancePriceFuture = CompletableFuture.supplyAsync(this::fetchBinancePrice, executor);
        CompletableFuture<HuobiPriceResponse> huobiPriceFuture = CompletableFuture.supplyAsync(this::fetchHuobiPrice, executor);

        // wait for all APIs to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(binancePriceFuture, huobiPriceFuture);
        allFutures.get();
        BinancePriceResponse binancePriceResponse = binancePriceFuture.get();
        HuobiPriceResponse huobiPriceResponse = huobiPriceFuture.get();

        // Shutdown the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // combine all prices for comparison
        List<MarketPriceElement> priceList = new ArrayList<>(binancePriceResponse.getPriceList().stream()
                .map(p -> new MarketPriceElement(p.getSymbol(), p.getBidPrice(), p.getAskPrice())).toList());
        priceList.addAll(huobiPriceResponse.getData().stream()
                .map(p -> new MarketPriceElement(p.getSymbol(), p.getBid(), p.getAsk())).toList());

        BigDecimal maxBidPrice;
        BigDecimal minAskPrice;
        for (CryptoSymbolEnum cryptoSymbol : CryptoSymbolEnum.values()) {
            maxBidPrice = priceList.stream().filter(p -> p.getSymbol().equalsIgnoreCase(cryptoSymbol.name()))
                    .map(MarketPriceElement::getBidPrice).max(BigDecimal::compareTo).orElse(null);
            minAskPrice = priceList.stream().filter(p -> p.getSymbol().equalsIgnoreCase(cryptoSymbol.name()))
                    .map(MarketPriceElement::getAskPrice).min(BigDecimal::compareTo).orElse(null);
            if (maxBidPrice == null || minAskPrice == null) {
                LOG.info("Something went wrong with the server, just wait for the next aggregation!!!");
                continue;
            }
            MarketPrice marketPrice = new MarketPrice();
            marketPrice.setCryptoSymbol(cryptoSymbol);
            marketPrice.setBidPrice(maxBidPrice);
            marketPrice.setAskPrice(minAskPrice);
            marketPrice.setDtReceived(LocalDateTime.now());
            upsertMarketPrice(marketPrice);
        }

    }

    private void upsertMarketPrice(MarketPrice marketPrice) {
        marketPriceRepository.upsertMarketPrice(
                marketPrice.getCryptoSymbol().name(),
                marketPrice.getBidPrice(),
                marketPrice.getAskPrice(),
                marketPrice.getDtReceived()
        );
    }

    private BinancePriceResponse fetchBinancePrice() {
        List<BinancePriceElement> priceList = fetchPriceList(CryptoProvider.BINANCE.getUrl(),
                new ParameterizedTypeReference<>() {});
        BinancePriceResponse response = new BinancePriceResponse();
        if (CollectionUtils.isEmpty(priceList)) {
            response.setPriceList(new ArrayList<>());
        } else {
            // filter allowed crypto symbols
            response.setPriceList(priceList.stream()
                    .filter(price -> CryptoSymbolEnum.contains(price.getSymbol())).toList());
        }
        return response;
    }

    private HuobiPriceResponse fetchHuobiPrice() {
        HuobiPriceResponse huobiPriceResponse = fetchPriceList(CryptoProvider.HUOBI.getUrl(),
                new ParameterizedTypeReference<>() {});
        if (huobiPriceResponse == null) {
            huobiPriceResponse = new HuobiPriceResponse();
        }
        if (CollectionUtils.isEmpty(huobiPriceResponse.getData())) {
            huobiPriceResponse.setData(new ArrayList<>());
        } else {
            // filter allowed crypto symbols
            huobiPriceResponse.setData(huobiPriceResponse.getData().stream()
                    .filter(price -> CryptoSymbolEnum.contains(price.getSymbol())).toList());
        }
        return huobiPriceResponse;
    }

    private <T> T fetchPriceList(String url, ParameterizedTypeReference<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<T> response = restTemplate.exchange(url,
                    HttpMethod.GET, null, responseType);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOG.error("Error during API call: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            LOG.error("Error during API call: {}", ex.getMessage());
        }
        return null;
    }
}
