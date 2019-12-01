package com.bilderlings.homework

import com.bilderlings.homework.builder.RateEntityBuilder
import com.bilderlings.homework.model.RateEntity
import com.bilderlings.homework.proxy.FixerRatesProxy
import com.bilderlings.homework.proxy.impl.FixerRatesProxyImpl
import com.bilderlings.homework.repository.CurrencyCalculatorRepository
import com.bilderlings.homework.service.CurrencyCalculatorService
import com.bilderlings.homework.service.impl.CurrencyCalculatorServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


@SpringBootTest
class CurrencyCalculatorServiceTest {

    @InjectMocks
    lateinit var currencyCalculatorService: CurrencyCalculatorServiceImpl

    @Mock
    lateinit var currencyCalculatorRepository: CurrencyCalculatorRepository

    @Mock
    lateinit var fixerRatesProxy: FixerRatesProxyImpl

    @Test
    fun getCalculateRate() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"

        Mockito.`when`(fixerRatesProxy.getRate(fromCurrency, toCurrency)).thenReturn(BigDecimal(0.9))

        val rateEntity = RateEntity(1, fromCurrency, toCurrency, BigDecimal(0.1))
        Mockito.`when`(currencyCalculatorRepository.findBy(fromCurrency, toCurrency)).thenReturn(Optional.of(rateEntity))

        val calculatedRate = currencyCalculatorService.getCalculatedRate(fromCurrency, toCurrency, BigDecimal(1000))
        Assertions.assertEquals(BigDecimal(800).setScale(2, RoundingMode.HALF_UP), calculatedRate.setScale(2, RoundingMode.HALF_UP))
    }



    @Test
    fun getCalculateRateLowFee() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"

        Mockito.`when`(fixerRatesProxy.getRate(fromCurrency, toCurrency)).thenReturn(BigDecimal(0.9))

        val rateEntity = RateEntity(1, fromCurrency, toCurrency, BigDecimal(0.01))
        Mockito.`when`(currencyCalculatorRepository.findBy(fromCurrency, toCurrency)).thenReturn(Optional.of(rateEntity))

        val calculatedRate = currencyCalculatorService.getCalculatedRate(fromCurrency, toCurrency, BigDecimal(1000))
        Assertions.assertEquals(BigDecimal(890).setScale(2, RoundingMode.HALF_UP), calculatedRate.setScale(2, RoundingMode.HALF_UP))
    }


    @Test
    fun getCalculateRateNoFeeSpecified() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"

        Mockito.`when`(fixerRatesProxy.getRate(fromCurrency, toCurrency)).thenReturn(BigDecimal(0.9))

        Mockito.`when`(currencyCalculatorRepository.findBy(fromCurrency, toCurrency)).thenReturn(Optional.empty())

        val calculatedRate = currencyCalculatorService.getCalculatedRate(fromCurrency, toCurrency, BigDecimal(1000))
        Assertions.assertEquals(BigDecimal(900).setScale(2, RoundingMode.HALF_UP), calculatedRate.setScale(2, RoundingMode.HALF_UP))
    }
}