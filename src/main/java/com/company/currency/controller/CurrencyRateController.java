package com.company.currency.controller;

import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.service.CurrencyRateService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange-rate")
public class CurrencyRateController implements CurrencyRateApi {

  private final CurrencyRateService currencyRateService;

  @Override
  @GetMapping(value = "/{target}/{base}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CurrencyRateDTO getRate(
      @PathVariable("target") String target,
      @PathVariable("base") String base,
      @RequestParam(value = "amount", required = false) BigDecimal amount,
      @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date) {
    return currencyRateService.getRate(target, base, amount, date.orElse(LocalDate.now()));
  }

  @Override
  @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, CurrencyDTO> getCurrencyStatistics() {
    return currencyRateService.getStatistics();
  }
}
