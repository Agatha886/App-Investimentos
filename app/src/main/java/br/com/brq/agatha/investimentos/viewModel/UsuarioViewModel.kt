package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.brq.agatha.investimentos.database.InvestimentosDataBase.Companion.getBatadaBase
import br.com.brq.agatha.investimentos.model.Usuario
import br.com.brq.agatha.investimentos.repository.UsuarioRepository

class UsuarioViewModel(context: Context): ViewModel(){
    private val dao = getBatadaBase(context).getUsuarioDao()
    private val repository = UsuarioRepository(dao)

    fun adicionaUsuario(usuario: Usuario){
        repository.adicionaUsuario(usuario)
    }

    fun buscaUsuario(id: Int): LiveData<Usuario>{
        return repository.retornaUsuario(id)
    }
}