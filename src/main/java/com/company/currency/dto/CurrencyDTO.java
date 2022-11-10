package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

   @Builder.Default
   @JsonProperty("amount_of_requests")
   private AtomicInteger amountOfRequests = new AtomicInteger();

  public void incAmountOfRequests() {
     amountOfRequests.incrementAndGet();
  }

   public Integer getAmountOfRequests() {
      return amountOfRequests.get();
   }
}
