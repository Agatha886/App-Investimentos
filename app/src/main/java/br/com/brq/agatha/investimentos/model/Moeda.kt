package br.com.brq.agatha.investimentos.model

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brq.agatha.investimentos.R
import java.io.Serializable
import java.lang.StringBuilder
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import javax.security.auth.callback.Callback

@Entity
class Moeda(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var buy: BigDecimal?,
    var sell: BigDecimal?,
    var variation: BigDecimal,
    var abreviacao: String?,
    var totalDeMoeda: Double = 0.00
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

    fun setTotalMoedaCompra(valorComprado: Double) {
        if (valorComprado >= 0.00)
            this.totalDeMoeda = totalDeMoeda + valorComprado
    }

    fun setTotalMoedaVenda(novoTotal: Double) {
        if (novoTotal >= 0.00)
            this.totalDeMoeda = novoTotal
    }

    fun setMoedaSimbulo(valorMoeda: BigDecimal): String {
        val decimalFormat = DecimalFormat("#0.00")
        val string = StringBuilder()
        if (abreviacao == "BTC") {
            string.append("₿ ").append(decimalFormat.format(valorMoeda))
        } else {
            val instance: Currency = Currency.getInstance(abreviacao)
            string.append(instance.symbol).append(" ").append(decimalFormat.format(valorMoeda))
        }
        return string.toString()
    }

}




