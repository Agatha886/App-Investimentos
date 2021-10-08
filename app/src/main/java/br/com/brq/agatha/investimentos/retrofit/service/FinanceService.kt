package br.com.brq.agatha.investimentos.retrofit.service

import br.com.brq.agatha.investimentos.model.Finance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceService {

    @GET("finance?key=835f00d7")
    fun buscaFinance(): Call<Finance>

    @GET("finance/quotations?key=835f00d7")
    fun buscaMoeda(@Query("fields") fields: String): Call<Finance>

}
