package com.bilderlings.homework.proxy.impl

import com.bilderlings.homework.pojo.FixerBaseRates
import com.bilderlings.homework.proxy.FixerRatesProxy
import com.bilderlings.homework.service.impl.CurrencyCalculatorServiceImpl
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.concurrent.TimeUnit


@Service
class FixerRatesProxyImpl : FixerRatesProxy {
    private final val cache: LoadingCache<String, FixerBaseRates>
    private final var logger: Logger = LoggerFactory.getLogger(CurrencyCalculatorServiceImpl::class.java)

    init {
        val loader: CacheLoader<String, FixerBaseRates>
        loader = object : CacheLoader<String, FixerBaseRates>() {
            override fun load(fromCurrency: String): FixerBaseRates {
                return getAllRatesFor(fromCurrency);
            }
        }
        cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build(loader)
    }

    override fun getRate(fromCurrency: String, toCurrency: String): BigDecimal {
        return cache.get(fromCurrency).rates[toCurrency]
                ?: throw RestClientException("'toCurrency' not found $toCurrency for - $fromCurrency")
    }

    fun getAllRatesFor(fromCurrency: String) : FixerBaseRates {
        logger.info("currency [$fromCurrency] loaded from fixer")
        //TODO base price is not available for free fixer plan.
        val uri = "http://data.fixer.io/api/latest?access_key=56978cce1c758c3ef8aeca3582a08fec"
        return RestTemplate().getForObject(uri, FixerBaseRates::class.java)
                ?: throw RestClientException("no response received from fixer")
    }
}