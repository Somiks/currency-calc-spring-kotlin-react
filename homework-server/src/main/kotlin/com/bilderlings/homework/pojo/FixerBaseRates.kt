package com.bilderlings.homework.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class FixerBaseRates(
        val base: String,
        val rates: Map<String, BigDecimal>
)