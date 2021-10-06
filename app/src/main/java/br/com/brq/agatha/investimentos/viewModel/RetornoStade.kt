package br.com.brq.agatha.investimentos.viewModel

import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.model.Moeda
import java.math.BigDecimal

sealed class RetornoStade {
    data class FalhaApi(var  listaMoeda: List<Moeda>) : RetornoStade()
    data class Sucesso(var  listaMoeda: List<Moeda>) : RetornoStade()

    data class FalhaCompra(var mensagemErro: String) : RetornoStade()
    data class SucessoCompra(var  saldo: BigDecimal) : RetornoStade()

    data class FalhaVenda(var mensagemErro: String) : RetornoStade()
    data class SucessoVenda(var totalMoedas: Double) : RetornoStade()

    companion object{
       val eventRetorno = MutableLiveData<RetornoStade>()
    }

}

