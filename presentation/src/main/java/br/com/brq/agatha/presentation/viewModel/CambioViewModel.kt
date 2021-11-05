package br.com.brq.agatha.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.model.Usuario
import br.com.brq.agatha.base.repository.MoedaDbDataSource
import br.com.brq.agatha.base.repository.UsuarioRepository
import br.com.brq.agatha.base.util.CoroutinesContextProvider
import br.com.brq.agatha.base.util.RetornoStadeCompraEVenda
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


    suspend fun adicionaUsuario(usuario: Usuario){
        io.launch {
            repositoryUsuario.adicionaUsuario(usuario)
        }
    }

    fun getSaldoDisponivel(): LiveData<BigDecimal> {
        val liveData = MutableLiveData<BigDecimal>()
        io.launch {
            val usuario = repositoryUsuario.getUsuario(idUsuario)
            liveData.postValue(usuario.saldoDisponivel)
        }
        return liveData
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
        val saldoAposCompra = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = repositoryUsuario.getUsuario(idUsuario)
            val novoSaldo = repositoryUsuario.calculaSaldoCompra(moeda, valorComprado, retornaUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            repositoryUsuario.modificaUsuario(retornaUsuario)
            saldoAposCompra.postValue(novoSaldo)
        }
        return saldoAposCompra
    }

    fun setSaldoAposVenda(
        idUsuario: Int,
        moeda: Moeda,
        valorVendaMoeda: String
    ): LiveData<BigDecimal> {
        val saldoAposVenda = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = repositoryUsuario.getUsuario(idUsuario)
            val novoSaldo = repositoryUsuario.calculaSaldoVenda(moeda, valorVendaMoeda, retornaUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            repositoryUsuario.modificaUsuario(retornaUsuario)
            saldoAposVenda.postValue(novoSaldo)
        }
        return saldoAposVenda
    }

    fun getTotalMoeda(nameMoeda: String): LiveData<Int> {
        val liveDate = MutableLiveData<Int>()
        io.launch {
            val moeda = dbDataSource.buscaMoedaNoBando(nameMoeda)
            liveDate.postValue(moeda.totalDeMoeda)
        }
        return liveDate
    }

    fun setToltalMoedaCompra(nameMoeda: String, valorDaCompra: Int) {
        io.launch {
            dbDataSource.setTotalMoedaAposCompra(nameMoeda, valorDaCompra)
        }
    }

    fun setTotalMoedaVenda(nameMoeda: String, valorDaCompra: Int) {
        io.launch {
            dbDataSource.setTotalMoedaAposVenda(nameMoeda, valorDaCompra)
        }
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


