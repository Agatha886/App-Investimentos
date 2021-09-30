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

class UsuarioRepository(private val daoUsuario: UsuarioDao){

    private val io = CoroutineScope(Dispatchers.IO)
    var quandoCompraSucesso: (saldoRestante: BigDecimal) -> Unit = {}
    var quandoFalhaCompra: (mensagem: String) -> Unit = {}


    fun usuario(id: Int): LiveData<BigDecimal> {
        val liveData = MutableLiveData<BigDecimal>()
        io.launch {
            val usuario = daoUsuario.retornaUsuario(id)
            withContext(Dispatchers.Main) {
                liveData.value = usuario.saldoDisponivel
            }
        }
        return liveData
    }

    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun calculaSaldoVenda(idUsuario: Int, moeda: Moeda, valorCompraMoeda: String): LiveData<BigDecimal>{
        val saldoAposSet = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.calculaSaldoVenda(moeda, valorCompraMoeda)
            setSaldo(idUsuario, novoSaldo)
            withContext(Dispatchers.Main){
                saldoAposSet.value = novoSaldo
            }
        }
        return saldoAposSet
    }

    fun setSaldo(idUsuario: Int, novoSaldo: BigDecimal){
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
        }
    }

    private fun modificaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.modificaUsuario(usuario)
        }
    }

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldoCompra(moeda, valor, usuario)

            if (novoSaldo > BigDecimal.ZERO) {
                withContext(Dispatchers.Main) {
                    quandoCompraSucesso(novoSaldo)
                }
            } else {
                withContext(Dispatchers.Main) {
                    quandoFalhaCompra("Valor de Compra Inválido")
                }
            }
        }
    }

    private fun calculaSaldoCompra(moeda: Moeda, valor: String, usuario: Usuario): BigDecimal {
        val valorDaCompra = BigDecimal(valor).multiply(moeda.buy)
        return usuario.saldoDisponivel.subtract(valorDaCompra)
    }

}