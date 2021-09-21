package br.com.brq.agatha.investimentos.retrofit.service

import br.com.brq.agatha.investimentos.model.Finance
import retrofit2.Call
import retrofit2.http.GET

interface FinanceService {
    @GET("finance")
    fun buscaFinance(): Call<Finance>
}