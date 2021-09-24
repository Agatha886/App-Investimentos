package br.com.brq.agatha.investimentos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.brq.agatha.investimentos.constantes.NOME_BANCO
import br.com.brq.agatha.investimentos.database.Migrations.Companion.migration_2_3
import br.com.brq.agatha.investimentos.database.Migrations.Companion.migration_3_4
import br.com.brq.agatha.investimentos.database.converter.BigDecimalConverter
import br.com.brq.agatha.investimentos.database.converter.ConversorTipoTransferencia
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.database.dao.TransferenciaDao
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Transferencia
import br.com.brq.agatha.investimentos.model.Usuario

@Database(entities = [Moeda::class, Usuario::class, Transferencia::class], version = 4, exportSchema = false)
@TypeConverters(BigDecimalConverter::class, ConversorTipoTransferencia::class)
abstract class InvestimentosDataBase :RoomDatabase(){
    abstract fun getMoedaDao(): MoedaDao
    abstract fun getUsuarioDao(): UsuarioDao
    abstract fun getTransferencia(): TransferenciaDao

    companion object{
        fun getBatadaBase(context: Context): InvestimentosDataBase {
            return Room.databaseBuilder(context, InvestimentosDataBase::class.java, NOME_BANCO)
                .addMigrations(migration_2_3, migration_3_4)
                .build()
        }
    }

}