package br.com.brq.agatha.investimentos.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {

    @TypeConverter
    fun paraBigDecimal(valor: BigDecimal?): String {
        return valor?.toString() ?: ""
    }

    @TypeConverter
    fun paraString(valor: String?): BigDecimal {
        return valor?.let { BigDecimal(it) } ?: BigDecimal.ZERO
    }

}