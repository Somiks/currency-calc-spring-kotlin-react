package com.bilderlings.homework.model

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class RateEntity(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        @NotBlank
        @Size(max = 3, min = 3)
        val fromCurrency: String,

        @NotBlank
        @Size(max = 3, min = 3)
        val toCurrency: String,

        @NotBlank
        @DecimalMin("0.0")
        val fee: BigDecimal
)