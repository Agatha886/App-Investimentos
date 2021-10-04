package br.com.brq.agatha.investimentos.model

import com.google.gson.annotations.SerializedName

class Currencies(
    @SerializedName("USD")
    val usd: Moeda,
    @SerializedName("EUR")
    val eur: Moeda,
    @SerializedName("GBP")
    val gbp: Moeda,
    @SerializedName("ARS")
    val ars: Moeda,
    @SerializedName("CAD")
    val cad: Moeda,
    @SerializedName("AUD")
    val aud: Moeda,
    @SerializedName("JPY")
    val jpy: Moeda,
    @SerializedName("CNY")
    val cny: Moeda,
    @SerializedName("BTC")
    val btc: Moeda )
