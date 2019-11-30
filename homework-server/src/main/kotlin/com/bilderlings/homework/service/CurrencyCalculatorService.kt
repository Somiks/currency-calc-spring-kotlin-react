package com.bilderlings.homework.service

import com.bilderlings.homework.model.RateEntity
import java.math.BigDecimal

interface CurrencyCalculatorService {
    fun addOrUpdateRate(rateEntity: RateEntity): RateEntity
    fun listRates(): List<RateEntity>
    fun getCalculatedRate(fromCurrency: String, toCurrency: String, amount: BigDecimal): BigDecimal
    fun removeRate(id: Long)
}