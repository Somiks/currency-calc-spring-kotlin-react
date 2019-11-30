package com.bilderlings.homework.controller

import com.bilderlings.homework.model.RateEntity
import com.bilderlings.homework.service.CurrencyCalculatorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.RoundingMode
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class CurrencyCalculatorController(
        private val currencyCalculatorService: CurrencyCalculatorService
) {

    @GetMapping("/calculate-rates")
    fun getCalculatedRates(@RequestParam fromCurrency: String, @RequestParam toCurrency: String,
                           @RequestParam amount: Long): BigDecimal {
        return currencyCalculatorService
                .getCalculatedRate(fromCurrency, toCurrency, BigDecimal(amount))
                .setScale(2, RoundingMode.HALF_UP)
    }

    @GetMapping("/list-rates")
    fun listRates(): List<RateEntity> {
        return currencyCalculatorService.listRates();
    }

    @PostMapping("/add-rate")
    fun addRate(@Valid @RequestBody rateEntity: RateEntity): RateEntity {
        return currencyCalculatorService.addOrUpdateRate(rateEntity);
    }
    @PostMapping("/remove-rate/{id}")
    fun addRate(@PathVariable id: Long): String {
        currencyCalculatorService.removeRate(id)
        return "{}"
    }
}
