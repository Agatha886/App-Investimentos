package br.com.brq.agatha.base.retrofit.service

import br.com.brq.agatha.domain.model.Finance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceService {

    @GET("finance?key=835f00d7")
    fun buscaFinance(): Call<Finance>


    @GET("finance/quotations?key=835f00d7")
    fun buscaMoeda(@Query("fields") fields: String): Call<Finance>

}
