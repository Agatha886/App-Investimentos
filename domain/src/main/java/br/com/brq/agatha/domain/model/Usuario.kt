package br.com.brq.agatha.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigDecimal

@Entity()
class Usuario(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var saldoDisponivel: BigDecimal = BigDecimal.ZERO
): Serializable{

    fun setSaldo(novoSaldo: BigDecimal){
        this.saldoDisponivel = novoSaldo
    }

}
