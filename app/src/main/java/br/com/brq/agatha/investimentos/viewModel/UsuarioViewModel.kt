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

    var quandoFalha: (mensagem: String) -> Unit = {}
    var quandoCompraSucesso: (valor: BigDecimal) -> Unit = {}
    var quandoVendaSucesso: (totalDeMoeda: Double) -> Unit = {}

    fun adicionaUsuario(usuario: Usuario) {
        usuarioRepository.adicionaUsuario(usuario)
    }

    fun usuario(id: Int): LiveData<Usuario> {
        return usuarioRepository.usuario(id)
    }

    fun calculaSaldoAposCompra(idUsuario: Int, moeda: Moeda, valor: String) {
        usuarioRepository.quandoCompraFalha = quandoFalha
        usuarioRepository.quandoCompraSucesso = quandoCompraSucesso
        usuarioRepository.compra(idUsuario,moeda, valor)
    }

    fun setSaldoCompra(idUsuario: Int, valorComprado: BigDecimal){
       usuarioRepository.setSaldo(idUsuario, valorComprado)
    }

    fun modificaUsuario(usuario: Usuario){
        usuarioRepository.modificaUsuario(usuario)
    }

}