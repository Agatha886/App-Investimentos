package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.MoedaRepository
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class CambioViewModel(context: Context) : ViewModel() {

    private val io = CoroutineScope(Dispatchers.IO)

    private val repositoryMoeda: MoedaRepository = MoedaRepository(context)
    private val repositoryUsuario = UsuarioRepository(context)
    // Tela De Câmbio Usuario

    fun adicionaUsuario(usuario: Usuario) {
        repositoryUsuario.adicionaUsuario(usuario)
    }

    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
        return repositoryUsuario.getSaldoDisponivel(id)
    }

    fun apagaTodos() {
        repositoryUsuario.apagaTodos()
    }

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = repositoryUsuario.getUsuario(idUsuario)
            val novoSaldo: BigDecimal = usuario.calculaSaldoCompra(moeda, valor)

            if (novoSaldo > BigDecimal.ZERO) {
                withContext(Dispatchers.Main) {
                  RetornoStade.eventRetorno.value = RetornoStade.SucessoCompra(novoSaldo)
                }
            } else {
                withContext(Dispatchers.Main) {
                    RetornoStade.eventRetorno.value = RetornoStade
                        .FalhaCompra("Valor de Compra Inválido")
                }
            }
        }
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


    // Tela De Câmbio Moeda

    fun getTotalMoeda(nameMoeda: String): LiveData<Double> {
        return repositoryMoeda.getTotalMoeda(nameMoeda)
    }

    fun setToltalMoedaCompra(nameMoeda: String, valorDaCompra: Double) {
        repositoryMoeda.setTotalMoedaAposCompra(nameMoeda, valorDaCompra)
    }

    fun setTotalMoedaVenda(nameMoeda: String, valorDaCompra: Double) {
        repositoryMoeda.setTotalMoedaAposVenda(nameMoeda, valorDaCompra)
    }

    fun venda(nameMoeda: String, valorDeVenda: String) {
        io.launch {
            val moeda = repositoryMoeda.buscaMoeda(nameMoeda)
            val valorTotalMoeda = moeda.totalDeMoeda.minus(BigDecimal(valorDeVenda).toDouble())

            if (valorTotalMoeda >= 00.0) {
                withContext(Dispatchers.Main) {
                    RetornoStade.eventRetorno.value = RetornoStade.SucessoVenda(valorTotalMoeda)
                }
            } else {
                withContext(Dispatchers.Main){
                    RetornoStade.eventRetorno.value = RetornoStade.FalhaVenda("Valor inválido")
                }
            }
        }
    }

}

