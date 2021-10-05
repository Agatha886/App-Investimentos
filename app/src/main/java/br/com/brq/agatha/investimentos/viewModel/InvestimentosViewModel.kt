package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.constantes.VALIDA_BUSCA_API
import br.com.brq.agatha.investimentos.model.Finance
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.MoedaRepository
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import br.com.brq.agatha.investimentos.retrofit.MoedasRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class InvestimentosViewModel(context: Context) : ViewModel() {

    private val io = CoroutineScope(Dispatchers.IO)

    private val eventRetornoApi = MutableLiveData<RetornoStade>()
    val viewEventRetornoApi: LiveData<RetornoStade> = eventRetornoApi

    private val eventRetornoVenda = MutableLiveData<RetornoStade>()
    val viewEventRetornoVenda: LiveData<RetornoStade> = eventRetornoVenda

    private val eventRetornoCompra = MutableLiveData<RetornoStade>()
    val viewEventRetornoCompra: LiveData<RetornoStade> = eventRetornoCompra

    private val repositoryMoeda: MoedaRepository = MoedaRepository(context)
    private val repositoryUsuario = UsuarioRepository(context)


    // Lista De Moedas Da Home
    fun buscaDaApi() {
        var finance: Finance?

        io.launch {
            val buscaMoedas = repositoryMoeda.buscaMoedas()

            try {
                VALIDA_BUSCA_API = true
                val call = MoedasRetrofit().retornaFinance()
                val resposta = call.execute()
                finance = resposta.body()
                atualizaBancoDeDados(buscaMoedas, finance)
                withContext(Dispatchers.Main) {
                    eventRetornoApi.value = RetornoStade.Sucesso(finance)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    eventRetornoApi.value = RetornoStade.Falha(buscaMoedas)
                }
            }
        }
    }

    private fun atualizaBancoDeDados(buscaMoedas: List<Moeda>, finance: Finance?) {
        if (buscaMoedas.isNullOrEmpty()) {
            repositoryMoeda.adicionaTodasAsMoedasNoBanco(finance)
            Log.i("TAG", "atualizaBancoDeDados: ENTROU NO ADICONA")
        } else {
            repositoryMoeda.modificaTotasAsMoedasNoBanco(finance)
            Log.i("TAG", "atualizaBancoDeDados: ENTROU NO MODIFICA")
        }
    }

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
            val novoSaldo: BigDecimal = repositoryUsuario.calculaSaldoCompra(moeda, valor, usuario)

            if (novoSaldo > BigDecimal.ZERO) {
                withContext(Dispatchers.Main) {
                   eventRetornoCompra.value = RetornoStade.Sucesso(novoSaldo)
                }
            } else {
                withContext(Dispatchers.Main) {
                    eventRetornoCompra.value = RetornoStade
                        .Falha("Valor de Compra Inválido")
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
                    eventRetornoVenda.value = RetornoStade.Sucesso(valorTotalMoeda)
                }
            } else {
                withContext(Dispatchers.Main){
                    eventRetornoVenda.value = RetornoStade.Falha("Valor inválido")
                }
            }
        }
    }


}

sealed class RetornoStade {
    data class Falha<T>(var `object`: T) : RetornoStade()
    data class Sucesso<T>(var  `object`: T) : RetornoStade()
}