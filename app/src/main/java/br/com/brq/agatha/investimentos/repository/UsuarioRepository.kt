package br.com.brq.agatha.investimentos.repository

import android.util.Log
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
    var quandoCompraSucesso: (saldoRestante: BigDecimal) -> Unit = {}

    fun usuario(id: Int): LiveData<Usuario> {
        val liveData = MutableLiveData<Usuario>()
        io.launch {
            val usuario = daoUsuario.retornaUsuario(id)
            withContext(Dispatchers.Main){
                liveData.value = usuario
            }
        }
        return liveData
    }

    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldo(moeda, valor, usuario)

            if (novoSaldo > BigDecimal.ZERO && novoSaldo!= null) {
                withContext(Dispatchers.Main) {
                    quandoCompraSucesso.invoke(novoSaldo)
                }
            }else {
                withContext(Dispatchers.Main) {
                    quandoCompraFalha("Valor de Compra Inválido")
                }
            }
        }
    }

   private fun calculaSaldo(moeda: Moeda, valor: String, usuario: Usuario): BigDecimal{
        val valorDaCompra = BigDecimal(valor).multiply(moeda.buy)
        val saldoAposCompra = usuario.saldoDisponivel.subtract(valorDaCompra)
        return saldoAposCompra
    }

    fun modificaUsuario(usuario: Usuario){
        io.launch {
            daoUsuario.modificaUsuario(usuario)
        }
    }


}