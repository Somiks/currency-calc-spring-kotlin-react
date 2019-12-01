package com.bilderlings.homework.proxy.impl

import com.bilderlings.homework.pojo.FixerBaseRates
import com.bilderlings.homework.proxy.FixerRatesProxy
import com.bilderlings.homework.service.impl.CurrencyCalculatorServiceImpl
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.concurrent.TimeUnit


@Service
class FixerRatesProxyImpl : FixerRatesProxy {
    private final var logger: Logger = LoggerFactory.getLogger(CurrencyCalculatorServiceImpl::class.java)
    @Value("\${fixer.url}")
    private val fixerUrl: String? = null

    private final val cache: LoadingCache<String, FixerBaseRates> by lazy {
        fixerUrl ?: throw IllegalArgumentException("fixer.url not specified")
        val loader: CacheLoader<String, FixerBaseRates>
        loader = object : CacheLoader<String, FixerBaseRates>() {
            override fun load(fromCurrency: String): FixerBaseRates {
                return getAllRatesFor(fromCurrency);
            }
        }
        CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build(loader)
    }

    override fun getRate(fromCurrency: String, toCurrency: String): BigDecimal {
        return cache.get(fromCurrency).rates[toCurrency]
                ?: throw RestClientException("'toCurrency' not found $toCurrency for - $fromCurrency")
    }

    fun getAllRatesFor(fromCurrency: String): FixerBaseRates {
        logger.info("currency [$fromCurrency] loaded from fixer")

        //TODO base price is not available for free fixer plan.
        return RestTemplate().getForObject(fixerUrl!!, FixerBaseRates::class.java)
                ?: throw RestClientException("no response received from fixer")
    }
}