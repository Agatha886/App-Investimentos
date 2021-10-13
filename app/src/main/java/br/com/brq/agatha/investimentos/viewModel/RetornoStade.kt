package br.com.brq.agatha.investimentos.viewModel

import br.com.brq.agatha.investimentos.model.Moeda
import java.math.BigDecimal

sealed class RetornoStadeApi {
    data class FalhaApi(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
    data class Sucesso(var  listaMoeda: List<Moeda>) : RetornoStadeApi()

}

sealed class RetornoStadeCompraEVenda {
    data class FalhaCompra(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoCompra(var valorGasto: BigDecimal) : RetornoStadeCompraEVenda()

    data class FalhaVenda(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoVenda(var totalMoedas: Double, var valorGastoNaVenda: String) : RetornoStadeCompraEVenda()

    object SemRetorno: RetornoStadeCompraEVenda()
}

