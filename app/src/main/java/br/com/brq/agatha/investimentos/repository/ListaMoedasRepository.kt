package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.constantes.VALIDA_BUSCA_API
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Currencies
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
                VALIDA_BUSCA_API = true
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                val finance: Finance? = resposta.body()
                val currencies = finance?.results?.currencies
                Log.i("TAG", "teste chamou no finance: $currencies")
                quandoBuscaNaAPI(finance)
            } catch (e: Exception) {
                quandoDarErroAoBuscar(e)
            }
        }
    }

    private fun quandoDarErroAoBuscar(e: Exception) {
        VALIDA_BUSCA_API = false
        Log.e("ERRO RETROFIT", "financeErro: ${e.message}")
        buscaNoBancoDedados()
    }

    private fun buscaNoBancoDedados() {
        val liveData = MutableLiveData<List<Moeda>>()
        io.launch {
            val buscaMoedas = daoMoeda.buscaTodasAsMoedas()
            withContext(Dispatchers.Main) {
                liveData.value = buscaMoedas
                quandoConexaoFalha(liveData)
            }
        }
    }

    private suspend fun quandoBuscaNaAPI(finance: Finance?) {
        val liveDataFinance = MutableLiveData<Finance>()
        atualizaListaMoedasDoBanco(finance)
        withContext(Dispatchers.Main) {
            liveDataFinance.value = finance
            quandoSucesso(liveDataFinance)
        }
    }

    private fun atualizaListaMoedasDoBanco(finance: Finance?) {
        if (daoMoeda.buscaTodasAsMoedas().isNullOrEmpty()) {
            adicionaTodasAsMoedas(finance)
        } else {
            modificaTotasAsMoedas(finance)
        }
    }

    private fun modificaTotasAsMoedas(finance: Finance?){
        finance?.results?.currencies?.usd?.let { modifica(it) }
        finance?.results?.currencies?.jpy?.let { modifica(it) }
        finance?.results?.currencies?.gbp?.let { modifica(it) }
        finance?.results?.currencies?.eur?.let { modifica(it) }
        finance?.results?.currencies?.cny?.let { modifica(it) }
        finance?.results?.currencies?.cad?.let { modifica(it) }
        finance?.results?.currencies?.btc?.let { modifica(it) }
        finance?.results?.currencies?.aud?.let { modifica(it) }
        finance?.results?.currencies?.ars?.let { modifica(it) }
    }

    private fun adicionaTodasAsMoedas(finance: Finance?){
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

}