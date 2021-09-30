package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.*

class ListaMoedasRepository(daoMoeda: MoedaDao) : MoedaRepository(daoMoeda) {

    var quandoConexaoFalha: (lista: LiveData<List<Moeda>>) -> Unit = {}
    var quandoSucesso: (finance: LiveData<Finance>) -> Unit = {}
    private val io = CoroutineScope(Dispatchers.IO)

    fun finance() {
        io.launch {
            try {
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                val finance: Finance? = resposta.body()
                quandoBuscaNaAPI(finance)
            } catch (e: Exception) {
                quandoDarErroAoBuscar(e)
            }
        }
    }

    private fun quandoDarErroAoBuscar(e: Exception) {
        Log.e("ERRO RETROFIT", "financeErro: ${e.message}")
        buscaNoBancoDedados()
    }

    private fun buscaNoBancoDedados() {
        val liveData = MutableLiveData<List<Moeda>>()
        io.launch {
            val buscaMoedas = daoMoeda.buscaMoedas()
            withContext(Dispatchers.Main) {
                liveData.value = buscaMoedas
                quandoConexaoFalha(liveData)
            }
        }
    }

    private suspend fun quandoBuscaNaAPI(finance: Finance?) {
        val liveDataFinance = MutableLiveData<Finance>()
        atualizaListaMoedas(finance)
        withContext(Dispatchers.Main) {
            liveDataFinance.value = finance
            quandoSucesso(liveDataFinance)
        }
    }

    fun atualizaListaMoedas(finance: Finance?) {
        if (daoMoeda.buscaMoedas().isNullOrEmpty()) {
            adicionaListaMoedas(finance)
        } else {
            modificaListaMoedas(finance)
        }
    }

}