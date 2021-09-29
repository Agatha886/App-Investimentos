package br.com.brq.agatha.investimentos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class UsuarioRepository(private val daoUsuario: UsuarioDao) {

    private val io = CoroutineScope(Dispatchers.IO)
    var quandoCompraFalha: (mensagem: String) -> Unit = {}
    var quandoVendaFalha: (mensagem: String) -> Unit = {}
    var quandoCompraSucesso: (saldoRestante: BigDecimal) -> Unit = {}
    var quandoVendaSucesso: (totalDeMoeda: Double) -> Unit = {}

    fun usuario(id: Int): LiveData<Usuario> {
        val liveData = MutableLiveData<Usuario>()
        io.launch {
            val usuario = daoUsuario.retornaUsuario(id)
            withContext(Dispatchers.Main) {
                liveData.value = usuario
            }
        }
        return liveData
    }

<<<<<<< HEAD
    fun setSaldoAposCompra(idUsuario: Int, novoSaldo: BigDecimal) {
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            setSaldo(retornaUsuario, novoSaldo)
        }
    }

    fun setSaldoAposVenda(idUsuario: Int, valorComprado: String, moeda: Moeda) {
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.setSaldoAposCompra(valorComprado, moeda)
            setSaldo(retornaUsuario, novoSaldo)
        }
    }

    fun setSaldo(usuario: Usuario, novoSaldo: BigDecimal) {
        io.launch {
            usuario.saldoDisponivel = novoSaldo
            daoUsuario.modificaUsuario(usuario)
        }
    }

=======
>>>>>>> parent of c4f28a0 (criação Fragment resposta)
    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldoCompra(moeda, valor, usuario)

            if (novoSaldo > BigDecimal.ZERO && novoSaldo != null) {
                withContext(Dispatchers.Main) {
                    quandoCompraSucesso(novoSaldo)
                }
            } else {
                withContext(Dispatchers.Main) {
                    quandoCompraFalha("Valor de Compra Inválido")
                }
            }
        }
    }

    private fun calculaSaldoCompra(moeda: Moeda, valor: String, usuario: Usuario): BigDecimal {
        val valorDaCompra = BigDecimal(valor).multiply(moeda.buy)
        val saldoAposCompra = usuario.saldoDisponivel.subtract(valorDaCompra)
        return saldoAposCompra
    }

    fun venda(moeda: Moeda, valor: String) {
        io.launch {
            val valorMoedaAposVenda = moeda.totalDeMoeda.minus(BigDecimal(valor).toDouble())
            if (valorMoedaAposVenda > 00.0 && valorMoedaAposVenda != null) {
                withContext(Dispatchers.Main) {
                    quandoVendaSucesso(valorMoedaAposVenda)
                }
            } else {
                quandoVendaFalha("Valor inválido")
            }
        }
    }

    fun modificaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.modificaUsuario(usuario)
        }
    }

}