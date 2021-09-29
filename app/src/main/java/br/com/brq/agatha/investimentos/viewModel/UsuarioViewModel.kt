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
    var quandoSucesso: (valor: BigDecimal) -> Unit = {}

    fun adicionaUsuario(usuario: Usuario) {
        usuarioRepository.adicionaUsuario(usuario)
    }

    fun usuario(id: Int): LiveData<Usuario> {
        return usuarioRepository.usuario(id)
    }

    fun calculaSaldoAposCompra(idUsuario: Int, moeda: Moeda, valor: String) {
        usuarioRepository.quandoCompraFalha = quandoFalha
        usuarioRepository.quandoCompraSucesso = quandoSucesso
        usuarioRepository.compra(idUsuario,moeda, valor)
    }

    fun setSaldo(idUsuario: Int, novoSaldo: BigDecimal){
        usuarioRepository.setSaldo(idUsuario, novoSaldo)
    }

    fun modificaUsuario(usuario: Usuario){
        usuarioRepository.modificaUsuario(usuario)
    }

}