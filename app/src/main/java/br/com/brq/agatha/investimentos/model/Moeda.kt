package br.com.brq.agatha.investimentos.model

import java.io.Serializable
import java.math.BigDecimal

class Moeda(val name: String,
            val buy:BigDecimal,
            val abreviacao: String,
            val sell: BigDecimal,
            val variation: String): Serializable
