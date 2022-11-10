package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Currency rate service response")
@JsonInclude(value = Include.NON_NULL)
public class CurrencyRateDTO {

    private BigDecimal rate;

    @JsonProperty("converted_amount")
    private BigDecimal convertedAmount;

    @ApiModelProperty("Currency pair")
    private String pair;

    @JsonProperty("chart_link")
    private String chartLink;
}
