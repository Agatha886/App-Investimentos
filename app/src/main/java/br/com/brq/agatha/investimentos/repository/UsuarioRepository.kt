package br.com.brq.agatha.investimentos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class UsuarioRepository(private val daoUsuario: UsuarioDao): TransacaoRepository(daoUsuario) {

    private val io = CoroutineScope(Dispatchers.IO)

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

    fun adicionaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.adiciona(usuario)
        }
    }

    fun setSaldo(idUsuario: Int, novoSaldo: BigDecimal){
        io.launch {
            val retornaUsuario = daoUsuario.retornaUsuario(idUsuario)
            retornaUsuario.setSaldo(novoSaldo)
            modificaUsuario(retornaUsuario)
        }
    }

    fun modificaUsuario(usuario: Usuario) {
        io.launch {
            daoUsuario.modificaUsuario(usuario)
        }
    }

}