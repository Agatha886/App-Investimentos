package br.com.brq.agatha.domain.model

import com.google.gson.annotations.SerializedName

class Results(
    @SerializedName("currencies")
    val currencies: Currencies
)