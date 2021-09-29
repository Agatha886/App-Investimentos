package br.com.brq.agatha.investimentos.repository

import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class MoedaRepository(val daoMoeda: MoedaDao) {
    private val io = CoroutineScope(Dispatchers.IO)

    fun modifica(moeda: Moeda) {
        io.launch {
            daoMoeda.modifica(moeda)
        }
    }

     fun atualizaListaMoedas(finance: Finance?) {
        if (daoMoeda.buscaMoedas().isNullOrEmpty()) {
            adicionaListaMoedas(finance)
        } else {
            modificaListaMoedas(finance)
        }
    }

    private fun modificaListaMoedas(finance: Finance?){
        finance?.results?.currencies?.usd?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.jpy?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.gbp?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.eur?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.cny?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.cad?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.btc?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.aud?.let { daoMoeda.modifica(it) }
        finance?.results?.currencies?.ars?.let { daoMoeda.modifica(it) }
    }

    private fun adicionaListaMoedas(finance: Finance?){
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