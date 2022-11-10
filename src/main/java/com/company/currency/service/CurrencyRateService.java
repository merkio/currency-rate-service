package com.company.currency.service;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.dto.ExchangeRatesResponse.DataSets;
import com.company.currency.exception.CurrencyRateException;
import com.company.currency.exception.IllegalArgumentsException;
import com.company.currency.util.CurrencyUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrencyRateService implements ICurrencyRateService {

  private static final String LINK_TEMPLATE = "https://www.xe.com/currencycharts/?from=%s&to=%s";

  private final Map<String, CurrencyDTO> currencies;
  private final ExchangeRatesApi exchangeRatesApi;

  public CurrencyRateService(ExchangeRatesApi exchangeRatesApi) {
    this.exchangeRatesApi = exchangeRatesApi;
    currencies = new ConcurrentHashMap<>();
    initCurrencies();
  }

  @Override
  public CurrencyRateDTO getRate(String target, String base, BigDecimal amount, LocalDate date) {
    log.info("Get currency rate for [{}] with base [{}] and target [{}]", date, base, target);

    if (!currencies.containsKey(base) || !currencies.containsKey(target)) {
      IllegalArgumentsException.throwException("Incorrect currencies: {} or {}", target, base);
    }

    BigDecimal rate;
    if (target.equalsIgnoreCase(base)) {
      rate = BigDecimal.ONE;
    } else {
      if (!target.equalsIgnoreCase("EUR") && !base.equalsIgnoreCase("EUR")) {
        BigDecimal baseRate = getCurrencyRate(base, date);
        BigDecimal targetRate = getCurrencyRate(target, date);

        rate = CurrencyUtils.calcRate(targetRate, baseRate);
        incCurrencyRequests(target);
        incCurrencyRequests(base);
      } else {
        if (target.equalsIgnoreCase("EUR")) {
          rate = getCurrencyRate(base, date);
          incCurrencyRequests(base);
        } else {
          rate = CurrencyUtils.calcRate(getCurrencyRate(target, date), BigDecimal.ONE);
          incCurrencyRequests(target);
        }
      }
    }

    CurrencyRateDTO rateDTO =
        CurrencyRateDTO.builder()
            .rate(rate)
            .pair(String.format("%s/%s", target, base))
            .chartLink(String.format(LINK_TEMPLATE, target, base))
            .build();

    if (Objects.nonNull(amount)) {
      rateDTO.setConvertedAmount(rate.multiply(amount));
    }

    return rateDTO;
  }

  @Override
  public Map<String, CurrencyDTO> getStatistics() {
    return currencies;
  }

  private BigDecimal getCurrencyRate(String currency, LocalDate date) {
    log.debug("Get rate for currency {} and date: {}", currency, date);
    ResponseEntity<ExchangeRatesResponse> response =
        exchangeRatesApi.getCurrencyRate(date, date, currency);

    if (Objects.isNull(response.getBody())) {
      throw new CurrencyRateException("No data from ECB");
    }
    ExchangeRatesResponse ratesResponse = response.getBody();
    return ratesResponse.getDataSets().stream()
        .findFirst()
        .orElse(new DataSets())
        .getSeries()
        .getFirstSeries()
        .getObservations()
        .getRate()
        .stream()
        .findFirst()
        .orElseThrow(() -> new CurrencyRateException("Unprocessed response from ECB"));
  }

  private void incCurrencyRequests(String currency) {
    currencies.get(currency).incAmountOfRequests();
  }

  private void initCurrencies() {
    List<String> currencyList =
        List.of(
            "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON", "SEK", "CHF", "ISK",
            "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD", "CNY", "HKD", "IDR", "ILS", "INR",
            "KRW", "MXN", "MYR", "NZD", "PHP", "SGD", "THB", "ZAR", "EUR");

    for (String currency : currencyList) {
      currencies.put(currency, CurrencyDTO.builder().build());
    }
  }
}
