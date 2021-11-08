package br.com.brq.agatha.base.util

import br.com.brq.agatha.domain.model.Moeda

sealed class RetornoStadeApi {
    data class SucessoRetornoBanco(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
    data class SucessoRetornoApi(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
}

sealed class RetornoStadeCompraEVenda {
    data class FalhaCompra(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoCompra(var valorDeMoedaComprado: String) : RetornoStadeCompraEVenda()

    data class FalhaVenda(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoVenda(var totalMoedas: Int, var valorGastoNaVenda: String) : RetornoStadeCompraEVenda()

    object SemRetorno: RetornoStadeCompraEVenda()
}

sealed class RetornoStadeFragments{
    data class VaiParaFragmentSucessoQuandoVendaSucesso(var mensagem: String) : RetornoStadeFragments()
    data class VaiParaFragmentSucessoQuandoCompraSucesso(var mensagem: String) : RetornoStadeFragments()
    data class VaiParaHomeQuandoMoedaRecebidaInvalida(var mensagem: String ): RetornoStadeFragments()
}


