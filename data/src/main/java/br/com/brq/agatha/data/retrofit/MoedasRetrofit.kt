package br.com.brq.agatha.data.retrofit

import br.com.brq.agatha.domain.model.Finance
import br.com.brq.agatha.data.retrofit.service.FinanceService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoedasRetrofit {

    private val retrofit: Retrofit = retrofitBase()
    private val serviceFinance: FinanceService = retrofit.create(FinanceService::class.java)

    private fun retrofitBase() = Retrofit.Builder()
        .baseUrl("https://api.hgbrasil.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun retornaFinance():Call<Finance>{
        return  serviceFinance.buscaFinance()
    }


    fun retornaMoeda(abreviacao: String): Call<Finance>{
        return serviceFinance.buscaMoeda(fields = "only_,results,$abreviacao")
    }

}