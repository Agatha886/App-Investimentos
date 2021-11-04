package br.com.brq.agatha.base.api

import br.com.brq.agatha.domain.model.Finance
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.base.database.dao.MoedaDao
import br.com.brq.agatha.base.retrofit.MoedasRetrofit

open class MoedaApiDataSource(moedaDao: MoedaDao) : MoedaDbDataSource(moedaDao) {
   fun getFinanceDaApi(): Finance? {
        val call = MoedasRetrofit().retornaFinance()
        val resposta = call.execute()
        return resposta.body()
    }

    suspend fun atualizaBancoDeDados(buscaMoedas: List<Moeda>, finance: Finance?) {
        if (buscaMoedas.isNullOrEmpty()) {
            adicionaTodasAsMoedasNoBanco(finance)
        } else {
            modificaTotasAsMoedasNoBanco(finance)
        }
    }

    private suspend fun  modificaTotasAsMoedasNoBanco(finance: Finance?) {
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

    private suspend fun adicionaTodasAsMoedasNoBanco(finance: Finance?) {
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