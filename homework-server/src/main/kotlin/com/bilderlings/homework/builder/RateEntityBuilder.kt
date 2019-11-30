package com.bilderlings.homework.builder

import com.bilderlings.homework.model.RateEntity

class RateEntityBuilder {
    fun build(id: Long, rateEntity: RateEntity): RateEntity {
        return RateEntity(id, rateEntity.fromCurrency, rateEntity.toCurrency, rateEntity.fee)
    }
}