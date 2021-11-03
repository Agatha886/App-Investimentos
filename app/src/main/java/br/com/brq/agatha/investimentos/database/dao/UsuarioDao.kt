package br.com.brq.agatha.investimentos.database.dao

import androidx.room.*
import br.com.brq.agatha.domain.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun adiciona(usuario: br.com.brq.agatha.domain.model.Usuario)

    @Query("SELECT * FROM usuario WHERE id = :id")
    fun retornaUsuario(id: Int): br.com.brq.agatha.domain.model.Usuario

    @Update
    fun modifica(usuario: br.com.brq.agatha.domain.model.Usuario)

    @Query("SELECT * FROM usuario")
    fun todos(): List<br.com.brq.agatha.domain.model.Usuario>

    @Delete
    fun delete(list: List<br.com.brq.agatha.domain.model.Usuario>)

}
