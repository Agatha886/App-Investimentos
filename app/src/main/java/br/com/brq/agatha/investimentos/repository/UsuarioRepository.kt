package br.com.brq.agatha.investimentos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.viewModel.base.CoroutinesContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class UsuarioRepository(
    private val daoUsuario: UsuarioDao,
    coroutinesContextProvider: CoroutinesContextProvider) {

    private val io = CoroutineScope(coroutinesContextProvider.io)

    fun getUsuario(idUsuario: Int): Usuario {
        return daoUsuario.retornaUsuario(idUsuario)
    }

    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
        val liveData = MutableLiveData<BigDecimal>()
        io.launch {
            val usuario = daoUsuario.retornaUsuario(id)
            Log.i("TAG", "getSaldoDisponivel: ${usuario.saldoDisponivel}")
            liveData.postValue(usuario.saldoDisponivel)
        }
        return liveData
    }

    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun setSaldoVendaERetornaSaldo(
        idUsuario: Int,
        moeda: Moeda,
        valorCompraMoeda: String
    ): LiveData<BigDecimal> {
        val saldoAposVenda = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.calculaSaldoVenda(moeda, valorCompraMoeda)
            setSaldoERetornaSaldo(idUsuario, novoSaldo)
            saldoAposVenda.postValue(novoSaldo)
        }
        return saldoAposVenda
    }

    fun setSaldoERetornaSaldo(idUsuario: Int, novoSaldo: BigDecimal): LiveData<BigDecimal> {
        io.launch {
            val usuario = daoUsuario.retornaUsuario(idUsuario)
            usuario.setSaldo(novoSaldo)
            modificaUsuario(usuario)
        }

        return tranformaSaldoEmLiveDate(novoSaldo)
    }


    private fun modificaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.modifica(usuario)
        }
    }

    private fun tranformaSaldoEmLiveDate(
        novoSaldo: BigDecimal
    ): LiveData<BigDecimal> {
        val saldoAposCompra = MutableLiveData<BigDecimal>()
        io.launch {
            saldoAposCompra.postValue(novoSaldo)
        }
        return saldoAposCompra
    }

}