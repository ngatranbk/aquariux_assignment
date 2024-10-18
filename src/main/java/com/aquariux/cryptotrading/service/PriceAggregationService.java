package com.aquariux.cryptotrading.service;

import com.aquariux.cryptotrading.constants.CryptoProvider;
import com.aquariux.cryptotrading.constants.CryptoSymbolEnum;
import com.aquariux.cryptotrading.dto.BinancePriceElement;
import com.aquariux.cryptotrading.dto.BinancePriceResponse;
import com.aquariux.cryptotrading.dto.HuobiPriceElement;
import com.aquariux.cryptotrading.dto.HuobiPriceResponse;
import com.aquariux.cryptotrading.repository.MarketPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAggregationService {

    private static final Logger LOG = LoggerFactory.getLogger(PriceAggregationService.class);

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    // Fetch data every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void fetchAndStorePrices() {
        BinancePriceResponse binancePrice = fetchBinancePrice();
        HuobiPriceResponse huobiPrice = fetchHuobiPrice();
    }

    private BinancePriceResponse fetchBinancePrice() {
        List<BinancePriceElement> priceList = fetchPriceList(CryptoProvider.BINANCE_URL,
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
        HuobiPriceResponse huobiPriceResponse = fetchPriceList(CryptoProvider.HOUBI_URL,
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
