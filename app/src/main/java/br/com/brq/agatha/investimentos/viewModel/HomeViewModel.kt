package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
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

class HomeViewModel(context: Context) : ViewModel() {

    private val io = CoroutineScope(Dispatchers.IO)

    private val eventRetornoApi = MutableLiveData<RetornoStade>()
    val viewEventRetornoApi: LiveData<RetornoStade> = eventRetornoApi
    private val repositoryMoeda: MoedaRepository = MoedaRepository(context)
    private val listaMoedasDaApi = mutableListOf<Moeda>()

    fun buscaDaApi() {
        var finance: Finance?

        io.launch {
            val buscaMoedas = repositoryMoeda.buscaMoedas()

            try {
                VALIDA_BUSCA_API = true
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                finance = resposta.body()
                atualizaBancoDeDados(buscaMoedas, finance)
                adicionaTodasAsMoedas(finance)
                withContext(Dispatchers.Main) {
                    eventRetornoApi.value = RetornoStade.Sucesso(listaMoedasDaApi)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    eventRetornoApi.value = RetornoStade.FalhaApi(buscaMoedas)
                }
            }
        }
    }

    private fun adicionaTodasAsMoedas(finance: Finance?) {
        listaMoedasDaApi.clear()
        finance?.results?.currencies?.usd?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.jpy?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.gbp?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.eur?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.cny?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.cad?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.btc?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.aud?.let { listaMoedasDaApi.add(it) }
        finance?.results?.currencies?.ars?.let { listaMoedasDaApi.add(it) }
    }

    private fun atualizaBancoDeDados(buscaMoedas: List<Moeda>, finance: Finance?) {
        if (buscaMoedas.isNullOrEmpty()) {
            repositoryMoeda.adicionaTodasAsMoedasNoBanco(finance)
        } else {
            repositoryMoeda.modificaTotasAsMoedasNoBanco(finance)
        }
    }

}