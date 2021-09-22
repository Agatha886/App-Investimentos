package br.com.brq.agatha.investimentos.model

import java.io.Serializable
import java.math.BigDecimal

class Moeda(
    var name: String,
    var buy: BigDecimal,
    var sell: BigDecimal,
    var variation: BigDecimal,
    var abreviacao: String
) : Serializable {

    fun setAbreviacao(): String {
        val abreviacao: String = when (this.name) {
            "Dollar" -> "USD"
            "Euro" -> "EUR"
            "Pound Sterling" -> "GBP"
            "Argentine Peso" -> "ARS"
            "Canadian Dollar" -> "CAD"
            "Australian Dollar" -> "AUD"
            "Japanese Yen" -> "JPY"
            "Renminbi" -> "CNY"
            "Bitcoin" -> "BTC"
            else -> "indefinido"
        }
        this.abreviacao = abreviacao
        return abreviacao
    }
}




