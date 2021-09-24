package br.com.brq.agatha.investimentos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.brq.agatha.investimentos.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun adiciona(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE id = :id")
    fun buscaUsuario(id: Int): LiveData<Usuario>

}
