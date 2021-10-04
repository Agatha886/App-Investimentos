package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.constantes.VALIDA_BUSCA_API
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.repository.MoedaRepository
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaDeMoedasViewModel(context: Context) : ViewModel() {

    private val io = CoroutineScope(Dispatchers.IO)
    private val repositoryMoeda: MoedaRepository = MoedaRepository(context)
    private val eventRetornoApi = MutableLiveData<RetornoApiStade>()
    val viewEventRetornoApi: LiveData<RetornoApiStade> = eventRetornoApi

    fun buscaDaApi() {
        var finance: Finance? = null
        io.launch {
            try {
                VALIDA_BUSCA_API = true
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                finance = resposta.body()
                withContext(Dispatchers.Main) {
                    eventRetornoApi.value = RetornoApiStade.sucesso(finance)
                }
            } catch (e: Exception) {
                val buscaMoedas = repositoryMoeda.buscaMoedas()
                atualizaBancoDeDados(buscaMoedas, finance)
                withContext(Dispatchers.Main){
                    eventRetornoApi.value = RetornoApiStade.falha(buscaMoedas)
                }
            }
        }
    }

    private fun atualizaBancoDeDados( buscaMoedas: List<Moeda>,finance: Finance?) {
        if (buscaMoedas.isNullOrEmpty()) {
            adicionaTodasAsMoedas(finance)
            Log.i("TAG", "atualizaBancoDeDados: ENTROU NO ADICONA")
        } else {
            modificaTotasAsMoedas(finance)
            Log.i("TAG", "atualizaBancoDeDados: ENTROU NO MODIFICA")
        }
    }

    private fun modificaTotasAsMoedas(finance: Finance?) {
        finance?.results?.currencies?.usd?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.jpy?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.gbp?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.eur?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.cny?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.cad?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.btc?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.aud?.let { repositoryMoeda.modifica(it) }
        finance?.results?.currencies?.ars?.let { repositoryMoeda.modifica(it) }
    }

    private fun adicionaTodasAsMoedas(finance: Finance?) {
        finance?.results?.currencies?.usd?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.jpy?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.gbp?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.eur?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.cny?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.cad?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.btc?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.aud?.let { repositoryMoeda.adiciona(it) }
        finance?.results?.currencies?.ars?.let { repositoryMoeda.adiciona(it) }
    }

}

sealed class RetornoApiStade() {
    data class falha(var listaMoedas: List<Moeda>) : RetornoApiStade()
    data class sucesso(var finance: Finance?) : RetornoApiStade()
}