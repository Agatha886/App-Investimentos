package br.com.brq.agatha.investimentos.model

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brq.agatha.investimentos.R
import java.io.Serializable
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
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
    var totalDeMoeda: Double = 0.00
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

    fun setTotalMoeda(novoTotal: Double) {
        if (novoTotal>=0.00)
            this.totalDeMoeda = totalDeMoeda + novoTotal
    }

}




