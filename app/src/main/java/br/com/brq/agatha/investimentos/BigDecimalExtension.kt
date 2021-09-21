package br.com.brq.agatha.investimentos

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

private const val PORTUGUES: String = "pt"
private const val BRASIL: String = "br"

fun BigDecimal.formatoMoedaBrasileira(): String {
    val formatoBrasileiro = DecimalFormat.getCurrencyInstance(Locale(PORTUGUES, BRASIL))
    return formatoBrasileiro.format(this)
}