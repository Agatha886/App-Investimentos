package br.com.brq.agatha.base.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {

    @TypeConverter
    fun paraBigDecimal(valor: BigDecimal?): String {
        return valor?.toString() ?: ""
    }

    @TypeConverter
    fun paraString(valor: String?): BigDecimal? {
        return if(valor.isNullOrBlank()) null else BigDecimal(valor)
    }

}