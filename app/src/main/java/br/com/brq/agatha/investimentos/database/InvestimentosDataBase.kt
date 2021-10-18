package br.com.brq.agatha.investimentos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.brq.agatha.investimentos.constantes.NOME_BANCO
import br.com.brq.agatha.investimentos.database.converter.BigDecimalConverter
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario

@Database(entities = [Moeda::class, Usuario::class], version = 10, exportSchema = false)
@TypeConverters(BigDecimalConverter::class)
abstract class InvestimentosDataBase :RoomDatabase(){
    abstract fun getMoedaDao(): MoedaDao
    abstract fun getUsuarioDao(): UsuarioDao

}