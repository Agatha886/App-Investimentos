package br.com.brq.agatha.investimentos.model

import androidx.room.*
import java.math.BigDecimal
@Entity(
    indices = [Index(value = arrayOf("idMoeda"), unique = true)],
    foreignKeys =
    [ForeignKey(
        entity = Moeda::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("idMoeda")
    )]
)
class Transferencia (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val valor: BigDecimal,
    var idMoeda:Int,
    var tipo:TipoTransferencia)
