package br.com.brq.agatha.investimentos.database

import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import androidx.room.Database
import androidx.room.TypeConverters
import br.com.brq.agatha.investimentos.database.converter.BigDecimalConverter
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import androidx.room.RoomDatabase
import br.com.brq.agatha.investimentos.database.converter.ConversorTipoTransferencia
import br.com.brq.agatha.investimentos.database.dao.TransferenciaDao
import br.com.brq.agatha.investimentos.model.Transferencia

@Database(entities = [Moeda::class, Usuario::class, Transferencia::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalConverter::class, ConversorTipoTransferencia::class)
abstract class InvestimentosDataBase :RoomDatabase(){
    abstract fun getMoedaDao(): MoedaDao
    abstract fun getUsuarioDao(): UsuarioDao
    abstract fun getTransferencia(): TransferenciaDao
}