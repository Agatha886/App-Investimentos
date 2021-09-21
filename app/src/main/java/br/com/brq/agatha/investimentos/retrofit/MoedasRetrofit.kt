package br.com.brq.agatha.investimentos.retrofit

import br.com.brq.agatha.investimentos.retrofit.service.FinanceService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MoedasRetrofit {

    val retrofit: Retrofit = retrofitBase()
    val serviceFinance = retrofit.create(FinanceService::class.java)

    private fun retrofitBase() = Retrofit.Builder()
        .baseUrl("https://api.hgbrasil.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}