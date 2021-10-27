package br.com.brq.agatha.investimentos.viewModel

import br.com.brq.agatha.investimentos.model.Moeda
import java.math.BigDecimal

sealed class RetornoStadeApi {
    data class SucessoRetornoBanco(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
    data class SucessoRetornoApi(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
}

sealed class RetornoStadeCompraEVenda {
    data class FalhaCompra(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoCompra(var valorDeMoedaComprado: String) : RetornoStadeCompraEVenda()

    data class FalhaVenda(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoVenda(var totalMoedas: Double, var valorGastoNaVenda: String) : RetornoStadeCompraEVenda()

    object SemRetorno: RetornoStadeCompraEVenda()
}

