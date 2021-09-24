package br.com.brq.agatha.investimentos.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity()
class Usuario(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val saldoDisponivel: BigDecimal)
