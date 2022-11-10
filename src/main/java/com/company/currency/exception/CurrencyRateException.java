package com.company.currency.exception;

import org.slf4j.helpers.MessageFormatter;

public class CurrencyRateException extends RuntimeException {

  public CurrencyRateException(String message) {
    super(message);
  }

  public CurrencyRateException(String message, Object... args) {
    super(MessageFormatter.arrayFormat(message, args).getMessage());
  }

  public static void throwException(String message, Object... args) {
    throw new CurrencyRateException(message, args);
  }
}
