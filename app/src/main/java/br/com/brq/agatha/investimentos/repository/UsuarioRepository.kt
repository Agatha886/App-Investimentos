package br.com.brq.agatha.investimentos.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class UsuarioRepository(private val context: Context){

    private val io = CoroutineScope(Dispatchers.IO)
    private val daoUsuario = InvestimentosDataBase.getBatadaBase(context).getUsuarioDao()


    fun getUsuario(idUsuario: Int): Usuario{
        return daoUsuario.retornaUsuario(idUsuario)
    }

    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
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

    fun apagaTodos(){
        io.launch {
            val todos = daoUsuario.todos()
            daoUsuario.delete(todos)
        }
    }

    fun getSaldoVenda(idUsuario: Int, moeda: Moeda, valorCompraMoeda: String): LiveData<BigDecimal>{
        val saldoAposVenda = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.calculaSaldoVenda(moeda, valorCompraMoeda)
            setSaldo(idUsuario, novoSaldo)
            withContext(Dispatchers.Main){
                saldoAposVenda.value = novoSaldo
            }
        }
        return saldoAposVenda
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
            daoUsuario.modifica(usuario)
        }
    }

}