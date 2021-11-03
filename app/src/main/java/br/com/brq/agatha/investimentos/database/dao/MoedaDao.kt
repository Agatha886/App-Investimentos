package br.com.brq.agatha.investimentos.database.dao

import androidx.room.*
import br.com.brq.agatha.domain.model.Moeda

@Dao
interface MoedaDao {

    @Query("SELECT * FROM moeda")
    fun buscaTodasAsMoedas(): List<br.com.brq.agatha.domain.model.Moeda>

    @Query("SELECT * FROM moeda WHERE name = :name")
    fun buscaMoeda(name: String): br.com.brq.agatha.domain.model.Moeda

    @Insert
    fun adiciona(moeda: br.com.brq.agatha.domain.model.Moeda)

    @Update
    fun modifica(moeda: br.com.brq.agatha.domain.model.Moeda)

    @Delete
    fun deleta(moeda: br.com.brq.agatha.domain.model.Moeda)

    @Query("DELETE FROM moeda")
    fun deletaTodasAsMoedas()
}
