package br.com.brq.agatha.investimentos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.MoedaDbDataSource
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CambioViewModel(
    private val dbDataSource: MoedaDbDataSource,
    private val repositoryUsuario: UsuarioRepository,
    coroutinesContextProvider: CoroutinesContextProvider
) : ViewModel() {

    private val io = CoroutineScope(coroutinesContextProvider.io)

    private val eventRetorno = MutableLiveData<RetornoStadeCompraEVenda>()
    val viewEventRetornoCompraEVenda: LiveData<RetornoStadeCompraEVenda> = eventRetorno


    fun getSaldoDisponivel(idUser: Int): LiveData<BigDecimal> {
        return repositoryUsuario.getSaldoDisponivel(idUser)
    }

    fun buscaMoedaDaApi() {
        io.launch {
            val call = MoedasRetrofit().retornaMoeda("USD")
            val resposta = call.execute()
            val finance = resposta.body()
//            Log.i("TAG", "buscaMoedaDaApi: ${finance?.results?.currencies?.name}")
        }
    }

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = repositoryUsuario.getUsuario(idUsuario)
            val novoSaldo: BigDecimal = usuario.calculaSaldoCompra(moeda, valor)

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

    fun setSaldoCompra(idUsuario: Int, valorComprado: BigDecimal): LiveData<BigDecimal>{
        return repositoryUsuario.setSaldo(idUsuario, valorComprado)
    }

    fun setSaldoVenda(
        idUsuario: Int,
        moeda: Moeda,
        valorMoedaComprado: String
    ): LiveData<BigDecimal> {
        return repositoryUsuario.getSaldoVenda(idUsuario, moeda, valorMoedaComprado)
    }


    fun getTotalMoeda(nameMoeda: String): LiveData<Double> {
        return dbDataSource.getTotalMoeda(nameMoeda)
    }

    fun setToltalMoedaCompra(nameMoeda: String, valorDaCompra: Double) {
        dbDataSource.setTotalMoedaAposCompra(nameMoeda, valorDaCompra)
    }

    fun setTotalMoedaVenda(nameMoeda: String, valorDaCompra: Double) {
        dbDataSource.setTotalMoedaAposVenda(nameMoeda, valorDaCompra)
    }

    fun venda(nameMoeda: String, quantidadeParaVenda: String) {
        io.launch {
            val moeda = dbDataSource.buscaMoeda(nameMoeda)
            val valorTotalMoeda = moeda.totalDeMoeda.minus(BigDecimal(quantidadeParaVenda).toDouble())

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


