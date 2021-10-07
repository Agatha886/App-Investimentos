package br.com.brq.agatha.investimentos.retrofit.service

import br.com.brq.agatha.investimentos.model.Currencie
import br.com.brq.agatha.investimentos.model.Finance
import retrofit2.Call
import retrofit2.http.GET

interface FinanceService {

    @GET("finance?key=835f00d7")
    fun buscaFinance(): Call<Finance>

    @GET("finance/quotations?&fields=only_,results,USD&key=835f00d7")
    fun buscaMoeda(): Call<Currencie>

}
