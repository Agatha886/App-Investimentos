package br.com.brq.agatha.presentation.viewModel

import br.com.brq.agatha.domain.model.Finance
import br.com.brq.agatha.domain.model.Moeda

class MoedaWrapper {
    fun agrupaTodasAsMoedasNaLista(finance: Finance?): List<Moeda>{
        val listaMoedaDaApi = arrayListOf<Moeda>()
        finance?.results?.currencies?.usd?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.jpy?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.gbp?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.eur?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.cny?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.cad?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.btc?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.aud?.let { listaMoedaDaApi.add(it) }
        finance?.results?.currencies?.ars?.let { listaMoedaDaApi.add(it) }
        return listaMoedaDaApi
    }


}