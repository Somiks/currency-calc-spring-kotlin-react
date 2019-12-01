package com.bilderlings.homework

import com.bilderlings.homework.model.RateEntity
import com.bilderlings.homework.proxy.impl.FixerRatesProxyImpl
import com.bilderlings.homework.repository.CurrencyCalculatorRepository
import com.bilderlings.homework.service.impl.CurrencyCalculatorServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClientException
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
        val rateFromFixer = BigDecimal(0.9)
        val rateEntity = RateEntity(1, fromCurrency, toCurrency, BigDecimal(0.1))
        val fee = Optional.of(rateEntity)
        val expectedResult = BigDecimal(800)
        validateCalculation(fromCurrency, toCurrency, rateFromFixer, fee, expectedResult)
    }


    @Test
    fun getCalculateRateLowFee() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"
        val rateFromFixer = BigDecimal(0.9)
        val rateEntity = RateEntity(1, fromCurrency, toCurrency, BigDecimal(0.01))
        val fee = Optional.of(rateEntity)
        val expectedResult = BigDecimal(890)
        validateCalculation(fromCurrency, toCurrency, rateFromFixer, fee, expectedResult)
    }


    @Test
    fun getCalculateRateNoFeeSpecified() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"
        val rateFromFixer = BigDecimal(0.9)
        val expectedResult = BigDecimal(900)
        validateCalculation(fromCurrency, toCurrency, rateFromFixer, Optional.empty(), expectedResult)
    }

    private fun validateCalculation(
            fromCurrency: String,
            toCurrency: String,
            rateFromFixer: BigDecimal,
            fee: Optional<RateEntity>,
            expectedResult: BigDecimal) {
        `when`(fixerRatesProxy.getRate(fromCurrency, toCurrency)).thenReturn(rateFromFixer)
        `when`(currencyCalculatorRepository.findBy(fromCurrency, toCurrency)).thenReturn(fee)
        val calculatedRate = currencyCalculatorService.getCalculatedRate(fromCurrency, toCurrency, BigDecimal(1000))
        assertEquals(expectedResult.setScale(2, RoundingMode.HALF_UP), calculatedRate.setScale(2, RoundingMode.HALF_UP))
    }

    @Test
    fun getCalculateRateNoFixerAvailable() {
        val fromCurrency = "EUR"
        val toCurrency = "CHF"

        `when`(fixerRatesProxy.getRate(fromCurrency, toCurrency)).thenThrow(RestClientException("Rest exception"))

        `when`(currencyCalculatorRepository.findBy(fromCurrency, toCurrency)).thenReturn(Optional.empty())

        assertThrows(RestClientException::class.java) {
            currencyCalculatorService.getCalculatedRate(fromCurrency, toCurrency, BigDecimal(1000))
        }
    }
}