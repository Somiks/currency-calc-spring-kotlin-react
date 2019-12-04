package com.bilderlings.homework.repository

import com.bilderlings.homework.model.RateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CurrencyCalculatorRepository : JpaRepository<RateEntity, Long> {

    @Query("FROM RateEntity WHERE fromCurrency = ?1 AND toCurrency = ?2")
    fun findBy(fromCurrency: String, toCurrency: String): RateEntity?
}