package br.com.brq.agatha.investimentos.viewModel

import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.model.Moeda
import java.math.BigDecimal

sealed class RetornoStadeApi {
    data class FalhaApi(var  listaMoeda: List<Moeda>) : RetornoStadeApi()
    data class Sucesso(var  listaMoeda: List<Moeda>) : RetornoStadeApi()

    companion object{
       val eventRetorno = MutableLiveData<RetornoStadeApi>()
    }
}

sealed class RetornoStadeCompraEVenda {
    data class FalhaCompra(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoCompra(var  saldo: BigDecimal) : RetornoStadeCompraEVenda()

    data class FalhaVenda(var mensagemErro: String) : RetornoStadeCompraEVenda()
    data class SucessoVenda(var totalMoedas: Double) : RetornoStadeCompraEVenda()

    object SemRetorno: RetornoStadeCompraEVenda()
}

