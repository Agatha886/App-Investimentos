package br.com.brq.agatha.investimentos.model

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

    fun calculaSaldoVenda(moeda: Moeda, valorVendaMoeda: String): BigDecimal{
        return saldoDisponivel.add(BigDecimal(valorVendaMoeda).multiply(moeda.sell))
    }

    fun calculaSaldoCompra(moeda: Moeda, valorComprado: String): BigDecimal {
        val valorDaCompra = BigDecimal(valorComprado).multiply(moeda.buy)
        return saldoDisponivel.subtract(valorDaCompra)
    }

}
