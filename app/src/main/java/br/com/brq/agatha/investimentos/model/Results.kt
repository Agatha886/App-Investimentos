package br.com.brq.agatha.investimentos.model

import com.google.gson.annotations.SerializedName

class Results(
    @SerializedName("currencies")
    val currencies: Currencies)