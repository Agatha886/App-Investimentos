package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase.Companion.getBatadaBase
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.UsuarioRepository
import java.math.BigDecimal

class UsuarioViewModel(context: Context) : ViewModel() {
    private val usuarioDao = getBatadaBase(context).getUsuarioDao()
    private val usuarioRepository = UsuarioRepository(usuarioDao)

    var quandoCompraFalha: (mensagem: String) -> Unit = {}
    var quandoCompraSucesso: (valor: BigDecimal) -> Unit = {}

    fun adicionaUsuario(usuario: Usuario) {
        usuarioRepository.adicionaUsuario(usuario)
    }

    fun getSaldoDisponivel(id: Int): LiveData<BigDecimal> {
        return usuarioRepository.getSaldoDisponivel(id)
    }

    fun apagaTodos(){
        usuarioRepository.apagaTodos()
    }

    fun validaSaldoUsuarioCompra(idUsuario: Int, moeda: Moeda, valor: String) {
        usuarioRepository.quandoFalhaCompra = quandoCompraFalha
        usuarioRepository.quandoCompraSucesso = quandoCompraSucesso
        usuarioRepository.compra(idUsuario,moeda, valor)
    }

    fun setSaldoCompra(idUsuario: Int, valorComprado: BigDecimal){
       usuarioRepository.setSaldo(idUsuario, valorComprado)
    }
    fun setSaldoVenda(idUsuario: Int, moeda: Moeda, valorMoedaComprado: String): LiveData<BigDecimal>{
        return usuarioRepository.getSaldoVenda(idUsuario, moeda, valorMoedaComprado)
    }

}