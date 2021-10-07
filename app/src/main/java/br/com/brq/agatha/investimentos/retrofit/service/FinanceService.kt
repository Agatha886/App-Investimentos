package br.com.brq.agatha.investimentos.retrofit.service

import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FinanceService {

    @GET("finance?key=835f00d7")
    fun buscaFinance(): Call<Finance>

    @GET("finance/quotations?&fields=only_,results,currencies,USD&key=835f00d7")
    fun buscaMoeda(): Call<Finance>

}