package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

class ListaMoedasRepository {

    var quandoConexaoFalha: () -> Unit = {}

    fun finance(): LiveData<Finance?>{
        val scope = CoroutineScope(Dispatchers.IO)
        val liveDate = MutableLiveData<Finance?>()

        scope.launch {
            try {
                val call = criaCall()
                val resposta = call.execute()
                val finance: Finance? = resposta.body()

                withContext(Dispatchers.Main) {
                    liveDate.value = finance
                }

            } catch (e: Exception) {
                Log.e("ERRO RETROFIT", "financeErro: ${e.message}")
                withContext(Dispatchers.Main) {
                    quandoConexaoFalha()
                }
            }
        }
        return liveDate

    }

    private fun criaCall(): Call<Finance> {
        return MoedasRetrofit().retornaFinance()
    }

}