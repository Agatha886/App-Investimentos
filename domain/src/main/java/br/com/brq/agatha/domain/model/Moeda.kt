package br.com.brq.agatha.domain.model

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brq.agatha.domain.R
import java.io.Serializable
import java.lang.StringBuilder
import java.math.BigDecimal
import java.util.*

@Entity
class Moeda(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var buy: BigDecimal?,
    var sell: BigDecimal?,
    var variation: BigDecimal,
    var abreviacao: String?,
    var totalDeMoeda: Int = 0
) : Serializable {

    fun setAbreviacao(): String {
        val abreviacao: String = when (this.name) {
            "Dollar" -> "USD"
            "Euro" -> "EUR"
            "Pound Sterling" -> "GBP"
            "Canadian Dollar" -> "CAD"
            "Australian Dollar" -> "AUD"
            "Japanese Yen" -> "JPY"
            "Renminbi" -> "CNY"
            "Bitcoin" -> "BTC"
            "Argentine Peso" -> "ARS"
            else -> "SemValor"
        }
        this.abreviacao = abreviacao
        return abreviacao
    }

    fun retornaCor(context: Context): Int {
        return when {
            variation < BigDecimal.ZERO -> {
                ContextCompat.getColor(context, R.color.red)
            }
            variation == BigDecimal.ZERO -> {
                ContextCompat.getColor(context, R.color.white)
            }
            else -> {
                ContextCompat.getColor(context, R.color.verde)
            }
        }

    }

    fun setTotalMoedaCompra(valorComprado: Int) {
        if (valorComprado >= 0)
            this.totalDeMoeda = totalDeMoeda + valorComprado
    }

    fun setTotalMoedaVenda(novoTotal: Int) {
        if (novoTotal >= 0)
            this.totalDeMoeda = novoTotal
    }

    fun setMoedaSimbulo(valorMoeda: Int): String {
        val string = StringBuilder()
        if (abreviacao == "BTC") {
            string.append("₿ ").append(valorMoeda)
        } else {
            val instance: Currency = Currency.getInstance(abreviacao)
            string.append(instance.symbol).append(" ").append(valorMoeda)
        }

        return string.toString().replace(",", ".")
    }

}




