package br.com.brq.agatha.investimentos.repository

import br.com.brq.agatha.investimentos.viewModel.RetornoStadeApi

interface MoedaRepository {
    fun buscaDaApi(retornoStadeApi: (retorno: RetornoStadeApi) -> Unit)
}