package br.com.brq.agatha.investimentos.database.converter

import android.util.Log
import androidx.room.TypeConverter
import br.com.brq.agatha.investimentos.model.TipoTransferencia
import java.lang.NullPointerException

class ConversorTipoTransferencia {
    @TypeConverter
    public fun paraString(tipo: TipoTransferencia): String{
        return tipo.toString()
    }

    @TypeConverter
    public fun paraTipoTransferencia(string: String): TipoTransferencia?{
        return try {
            TipoTransferencia.valueOf(string)
        }catch (e: NullPointerException){
            Log.e("ERRO TIPO HISTORICO", "Tipo telefone Indefinido - ${e.message}")
            return TipoTransferencia.INDEFINIDO
        }

    }

}
