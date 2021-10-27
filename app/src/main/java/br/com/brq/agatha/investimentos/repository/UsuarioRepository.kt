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
    coroutinesContextProvider: CoroutinesContextProvider
) {

    private val io = CoroutineScope(coroutinesContextProvider.io)

    fun getUsuario(idUsuario: Int): Usuario {
        return daoUsuario.retornaUsuario(idUsuario)
    }

    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
        val liveData = MutableLiveData<BigDecimal>()
        io.launch {
            val usuario = daoUsuario.retornaUsuario(id)
            liveData.postValue(usuario.saldoDisponivel)
        }
        return liveData
    }

    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun getSaldoAposVenda(
        idUsuario: Int,
        moeda: Moeda,
        valorVendaMoeda: String
    ): LiveData<BigDecimal> {
        val saldoAposVenda = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.calculaSaldoVenda(moeda, valorVendaMoeda)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
            saldoAposVenda.postValue(novoSaldo)
        }
        return saldoAposVenda
    }

    fun getSaldoAposCompra(
        idUsuario: Int,
        valorComprado: String,
        moeda: Moeda
    ): LiveData<BigDecimal> {
        val saldoAposCompra = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = retornaUsuario.calculaSaldoCompra(moeda, valorComprado)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
            saldoAposCompra.postValue(novoSaldo)
        }
        return saldoAposCompra
    }


    private fun modificaUsuario(usuario: Usuario) {
        daoUsuario.modifica(usuario)
    }

}