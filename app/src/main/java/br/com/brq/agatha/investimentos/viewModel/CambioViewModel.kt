package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.MoedaDbDataSource
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class CambioViewModel(context: Context) : ViewModel() {

    private val io = CoroutineScope(Dispatchers.IO)

    private val dbDataSource =  MoedaDbDataSource(context)
    private val repositoryUsuario = UsuarioRepository(context)
    private val eventRetorno = MutableLiveData<RetornoStadeCompraEVenda>()
    val viewEventRetornoCompraEVenda: LiveData<RetornoStadeCompraEVenda> = eventRetorno


    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
        return repositoryUsuario.getSaldoDisponivel(id)
    }

    fun buscaMoedaDaApi(){
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

            if (novoSaldo > BigDecimal.ZERO) {
                withContext(Dispatchers.Main) {
                  eventRetorno.value = RetornoStadeCompraEVenda.SucessoCompra(BigDecimal(valor))
                }
            } else {
                withContext(Dispatchers.Main) {
                    eventRetorno.value = RetornoStadeCompraEVenda
                        .FalhaCompra("Valor de Compra Inválido")
                }
            }
        }
    }

    fun setEventRetornoComoSem(){
        eventRetorno.value = RetornoStadeCompraEVenda.SemRetorno
    }

    fun setSaldoCompra(idUsuario: Int, valorComprado: BigDecimal) {
        repositoryUsuario.setSaldo(idUsuario, valorComprado)
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
                withContext(Dispatchers.Main) {
                    eventRetorno.value = RetornoStadeCompraEVenda.SucessoVenda(valorTotalMoeda, quantidadeParaVenda)
                }
            } else {
                withContext(Dispatchers.Main){
                    eventRetorno.value = RetornoStadeCompraEVenda.FalhaVenda("Valor inválido")
                }
            }
        }
    }

}

