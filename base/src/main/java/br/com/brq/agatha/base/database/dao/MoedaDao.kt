package br.com.brq.agatha.base.database.dao

import androidx.room.*
import br.com.brq.agatha.domain.model.Moeda

@Dao
interface MoedaDao {

    @Query("SELECT * FROM moeda")
    fun buscaTodasAsMoedas(): List<Moeda>

    @Query("SELECT * FROM moeda WHERE name = :name")
    fun buscaMoeda(name: String): Moeda

    @Insert
    fun adiciona(moeda: Moeda)

    @Update
    fun modifica(moeda: Moeda)

    @Delete
    fun deleta(moeda: Moeda)

    @Query("DELETE FROM moeda")
    fun deletaTodasAsMoedas()
}
