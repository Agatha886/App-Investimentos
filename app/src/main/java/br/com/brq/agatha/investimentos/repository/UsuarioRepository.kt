package br.com.brq.agatha.investimentos.repository

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

    fun setSaldoAposVenda(
        idUsuario: Int,
        moeda: Moeda,
        valorVendaMoeda: String
    ): LiveData<BigDecimal> {
        val saldoAposVenda = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldoVenda(moeda, valorVendaMoeda, retornaUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
            saldoAposVenda.postValue(novoSaldo)
        }
        return saldoAposVenda
    }

    fun setSaldoAposCompra(
        idUsuario: Int,
        valorComprado: String,
        moeda: Moeda
    ): LiveData<BigDecimal> {
        val saldoAposCompra = MutableLiveData<BigDecimal>()
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldoCompra(moeda, valorComprado, retornaUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
            saldoAposCompra.postValue(novoSaldo)
        }
        return saldoAposCompra
    }


    private fun modificaUsuario(usuario: Usuario) {
        daoUsuario.modifica(usuario)
    }

    fun calculaSaldoCompra(moeda: Moeda, valorComprado: String, user: Usuario): BigDecimal {
        val valorDaCompra = BigDecimal(valorComprado).multiply(moeda.buy)
        return user.saldoDisponivel.subtract(valorDaCompra)
    }

    fun calculaSaldoVenda(moeda: Moeda, valorVendaMoeda: String, user: Usuario): BigDecimal{
        return user.saldoDisponivel.add(BigDecimal(valorVendaMoeda).multiply(moeda.sell))
    }
    fun calculaTotalDeMoedasVenda(moeda: Moeda, quantidadeParaVenda: String): Int{
        return moeda.totalDeMoeda.minus(BigDecimal(quantidadeParaVenda).toInt())
    }


}