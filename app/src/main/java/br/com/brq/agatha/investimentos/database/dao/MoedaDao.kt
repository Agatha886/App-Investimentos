package br.com.brq.agatha.investimentos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.brq.agatha.investimentos.model.Moeda

@Dao
interface MoedaDao {

    @Query("SELECT * FROM moeda")
    fun buscaMoedas(): List<Moeda>

    @Insert
    fun adiciona(moeda: Moeda)

    @Update
    fun modifica(moeda: Moeda)

    @Delete
    fun deletaMoeda(moeda: Moeda)

    @Query("DELETE FROM moeda")
    fun deletaTodasMoedas()
}
