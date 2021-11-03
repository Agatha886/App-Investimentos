package br.com.brq.agatha.investimentos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.model.Usuario
import br.com.brq.agatha.data.repository.MoedaDbDataSource
import br.com.brq.agatha.data.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CambioViewModel(
    private val dbDataSource: MoedaDbDataSource,
    private val repositoryUsuario: UsuarioRepository,
    coroutinesContextProvider: CoroutinesContextProvider,
    private val idUsuario: Int
) : ViewModel() {

    private val io = CoroutineScope(coroutinesContextProvider.io)

    private val eventRetorno = MutableLiveData<RetornoStadeCompraEVenda>()
    val viewEventRetornoCompraEVenda: LiveData<RetornoStadeCompraEVenda> = eventRetorno


    fun adicionaUsuario(usuario: Usuario){
        repositoryUsuario.adicionaUsuario(usuario)
    }

    fun getSaldoDisponivel(): LiveData<BigDecimal> {
        return repositoryUsuario.getSaldoDisponivel(idUsuario)
    }


    fun compra(moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = repositoryUsuario.getUsuario(idUsuario)
            val novoSaldo: BigDecimal = repositoryUsuario.calculaSaldoCompra(moeda, valor, usuario)

            if (novoSaldo >= BigDecimal.ZERO) {
                eventRetorno.postValue(RetornoStadeCompraEVenda.SucessoCompra(valor))
            } else {
                eventRetorno.postValue(
                    RetornoStadeCompraEVenda
                        .FalhaCompra("Valor de Compra Inválido")
                )
            }
        }
    }

    fun setEventRetornoComoSem() {
        eventRetorno.value = RetornoStadeCompraEVenda.SemRetorno
    }

    fun setSaldoCompra(valorComprado: String, moeda: Moeda): LiveData<BigDecimal>{
        return repositoryUsuario.setSaldoAposCompra(idUsuario = idUsuario, valorComprado = valorComprado, moeda = moeda)
    }

    fun setSaldoVenda(
        idUsuario: Int,
        moeda: Moeda,
        valorMoedaComprado: String
    ): LiveData<BigDecimal> {
        return repositoryUsuario.setSaldoAposVenda(idUsuario, moeda, valorMoedaComprado)
    }


    fun getTotalMoeda(nameMoeda: String): LiveData<Int> {
        return dbDataSource.getTotalMoeda(nameMoeda)
    }

    fun setToltalMoedaCompra(nameMoeda: String, valorDaCompra: Int) {
        dbDataSource.setTotalMoedaAposCompra(nameMoeda, valorDaCompra)
    }

    fun setTotalMoedaVenda(nameMoeda: String, valorDaCompra: Int) {
        dbDataSource.setTotalMoedaAposVenda(nameMoeda, valorDaCompra)
    }

    fun venda(nameMoeda: String, quantidadeParaVenda: String) {
        io.launch {
            val moeda = dbDataSource.buscaMoedaNoBando(nameMoeda)
            val valorTotalMoeda = repositoryUsuario.calculaTotalDeMoedasVenda(moeda, quantidadeParaVenda)

            if (valorTotalMoeda >= 00.0) {
                eventRetorno.postValue(
                    RetornoStadeCompraEVenda.SucessoVenda(
                        valorTotalMoeda,
                        quantidadeParaVenda
                    )
                )
            } else {
                eventRetorno.postValue(RetornoStadeCompraEVenda.FalhaVenda("Valor inválido"))
            }
        }
    }
}


