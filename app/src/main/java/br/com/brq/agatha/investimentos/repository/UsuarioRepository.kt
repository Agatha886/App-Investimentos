package br.com.brq.agatha.investimentos.repository

import androidx.lifecycle.LiveData
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioRepository(private val daoUsuario: UsuarioDao){

    fun retornaUsuario(id: Int):LiveData<Usuario>{
        return daoUsuario.buscaUsuario(id)
    }

    fun adicionaUsuario(usuario: Usuario){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            daoUsuario.adiciona(usuario)
        }
    }

}