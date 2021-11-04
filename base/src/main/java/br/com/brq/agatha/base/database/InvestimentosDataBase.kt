package br.com.brq.agatha.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.brq.agatha.base.database.converter.BigDecimalConverter
import br.com.brq.agatha.base.database.dao.MoedaDao
import br.com.brq.agatha.base.database.dao.UsuarioDao

@Database(entities = [br.com.brq.agatha.domain.model.Moeda::class, br.com.brq.agatha.domain.model.Usuario::class], version = 11, exportSchema = false)
@TypeConverters(BigDecimalConverter::class)

abstract class InvestimentosDataBase :RoomDatabase(){
    abstract fun getMoedaDao(): MoedaDao
    abstract fun getUsuarioDao(): UsuarioDao
}