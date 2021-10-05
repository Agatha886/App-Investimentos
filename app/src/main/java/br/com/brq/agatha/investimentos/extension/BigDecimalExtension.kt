package br.com.brq.agatha.investimentos.extension

import java.lang.String.format
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

private const val PORTUGUES: String = "pt"
private const val BRASIL: String = "br"

fun BigDecimal.formatoMoedaBrasileira(): String {
    val formatoBrasileiro = DecimalFormat.getCurrencyInstance(Locale(PORTUGUES, BRASIL))
    return formatoBrasileiro.format(this)
}


fun BigDecimal.formatoPorcentagem(): String {
    val formato: String = format(Locale(PORTUGUES, BRASIL), "%.2f", this)
    return "$formato%"
}

