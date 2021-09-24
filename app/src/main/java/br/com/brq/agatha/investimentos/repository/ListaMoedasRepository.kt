package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaMoedasRepository(private val daoMoeda: MoedaDao) {

    var quandoConexaoFalha: (lista: LiveData<List<Moeda>>) -> Unit = {}
    var quandoSucesso: (finance: LiveData<Finance>) -> Unit = {}

    private fun modificaBanco(finance: Finance?) {
        daoMoeda.deletaTodasMoedas()
        finance?.results?.currencies?.usd?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.jpy?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.gbp?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.eur?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.cny?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.cad?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.btc?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.aud?.let { daoMoeda.adiciona(it) }
        finance?.results?.currencies?.ars?.let { daoMoeda.adiciona(it) }
    }

   fun finance(){
        val liveDataFinance = MutableLiveData<Finance>()
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            try {
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                val finance: Finance? = resposta.body()
                modificaBanco(finance)
                withContext(Dispatchers.Main) {
                   liveDataFinance.value = finance
                    quandoSucesso(liveDataFinance)
                }

            } catch (e: Exception) {
                Log.e("ERRO RETROFIT", "financeErro: ${e.message}")
                withContext(Dispatchers.Main) {
                    quandoConexaoFalha(daoMoeda.buscaMoedas())
                }
            }
        }
    }
}