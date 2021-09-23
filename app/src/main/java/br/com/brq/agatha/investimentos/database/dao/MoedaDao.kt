package br.com.brq.agatha.investimentos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.brq.agatha.investimentos.model.Moeda

@Dao
interface MoedaDao {

    @Query("SELECT * FROM moeda")
    fun buscaMoedas(): LiveData<List<Moeda>>

    @Insert
    fun salva(moeda: Moeda)
}
