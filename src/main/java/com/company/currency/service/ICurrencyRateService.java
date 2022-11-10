package com.company.currency.service;

import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ICurrencyRateService {

  CurrencyRateDTO getRate(String base, String target, BigDecimal amount, LocalDate date);

  Map<String, CurrencyDTO> getStatistics();
}
