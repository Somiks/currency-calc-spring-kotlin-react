package com.bilderlings.homework.proxy

import java.math.BigDecimal

interface FixerRatesProxy {
    fun getRate(fromCurrency: String, toCurrency: String): BigDecimal
}