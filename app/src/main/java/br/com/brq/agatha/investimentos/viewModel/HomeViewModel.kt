package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
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

    private val repositoryMoeda: MoedaRepository = MoedaRepository(context)
    private val listaMoedasDaApi = mutableListOf<Moeda>()
    var quandoFinaliza:() -> Unit ={}

    fun buscaDaApi() {
        var finance: Finance?
        io.launch {
            val buscaMoedas = repositoryMoeda.buscaMoedas()
            try {
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                finance = resposta.body()
                atualizaBancoDeDados(buscaMoedas, finance)
                agrupaTodasAsMoedasNaLista(finance)
                withContext(Dispatchers.Main) {
                    RetornoStade.eventRetorno.value = RetornoStade.Sucesso(listaMoedasDaApi)
                    quandoFinaliza()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    RetornoStade.eventRetorno.value = RetornoStade.FalhaApi(buscaMoedas)
                    quandoFinaliza()
                }
            }
        }
    }

    private fun agrupaTodasAsMoedasNaLista(finance: Finance?) {
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