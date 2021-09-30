package br.com.brq.agatha.investimentos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    fun adiciona(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE id = :id")
    fun retornaUsuario(id: Int): Usuario

    @Update
    fun modifica(usuario: Usuario)


}
