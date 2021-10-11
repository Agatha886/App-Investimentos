package br.com.brq.agatha.investimentos.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import br.com.brq.agatha.investimentos.viewModel.RetornoStadeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class MoedaApiDataSource(context: Context): MoedaDbDataSource(context){

    private val io = CoroutineScope(Dispatchers.IO)
    private val listaMoedasDaApi = mutableListOf<Moeda>()

    fun buscaDaApi(retornoStadeApi: (retorno: RetornoStadeApi) -> Unit){
        var finance: Finance?
        io.launch {
            val moedasDoBanco = buscaMoedasNoBanco()
            try {
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                finance = resposta.body()
                atualizaBancoDeDados(moedasDoBanco, finance)
                agrupaTodasAsMoedasNaLista(finance)

                withContext(Dispatchers.Main) {
                    retornoStadeApi(RetornoStadeApi.Sucesso(listaMoedasDaApi))
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    retornoStadeApi(RetornoStadeApi.FalhaApi(moedasDoBanco))
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
            adicionaTodasAsMoedasNoBanco(finance)
        } else {
            modificaTotasAsMoedasNoBanco(finance)
        }
    }

    fun modificaTotasAsMoedasNoBanco(finance: Finance?) {
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

    fun adicionaTodasAsMoedasNoBanco(finance: Finance?) {
        finance?.results?.currencies?.usd?.let { adiciona(it) }
        finance?.results?.currencies?.jpy?.let { adiciona(it) }
        finance?.results?.currencies?.gbp?.let { adiciona(it) }
        finance?.results?.currencies?.eur?.let { adiciona(it) }
        finance?.results?.currencies?.cny?.let { adiciona(it) }
        finance?.results?.currencies?.cad?.let { adiciona(it) }
        finance?.results?.currencies?.btc?.let { adiciona(it) }
        finance?.results?.currencies?.aud?.let { adiciona(it) }
        finance?.results?.currencies?.ars?.let { adiciona(it) }
    }

}