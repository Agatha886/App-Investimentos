package br.com.brq.agatha.investimentos.repository

import android.util.Log
import br.com.brq.agatha.investimentos.database.dao.UsuarioDao
import br.com.brq.agatha.investimentos.model.Moeda
import br.com.brq.agatha.investimentos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

open class TransacaoRepository(private val daoUsuario: UsuarioDao) {

    private val io = CoroutineScope(Dispatchers.IO)
    var quandoFalhaCompra: (mensagem: String) -> Unit = {}
    var quandoFalhaVenda: (mensagem: String) -> Unit = {}
    var quandoCompraSucesso: (saldoRestante: BigDecimal) -> Unit = {}
    var quandoVendaSucesso: (totalDeMoeda: Double) -> Unit = {}

    fun compra(idUsuario: Int, moeda: Moeda, valor: String) {
        io.launch {
            val usuario: Usuario = daoUsuario.retornaUsuario(idUsuario)
            val novoSaldo = calculaSaldoCompra(moeda, valor, usuario)

            if (novoSaldo > BigDecimal.ZERO) {
                withContext(Dispatchers.Main) {
                    quandoCompraSucesso(novoSaldo)
                }
            } else {
                withContext(Dispatchers.Main) {
                    quandoFalhaCompra("Valor de Compra Inválido")
                }
            }
        }
    }

    private fun calculaSaldoCompra(moeda: Moeda, valor: String, usuario: Usuario): BigDecimal {
        val valorDaCompra = BigDecimal(valor).multiply(moeda.buy)
        return usuario.saldoDisponivel.subtract(valorDaCompra)
    }

    fun venda(moeda: Moeda, valorDeVenda: String) {
        io.launch {
            val valorTotalMoeda = moeda.totalDeMoeda.minus(BigDecimal(valorDeVenda).toDouble())
            if (valorTotalMoeda > 00.0) {
                withContext(Dispatchers.Main) {
                    quandoVendaSucesso(valorTotalMoeda)
                    Log.i("TAG", "venda: $valorTotalMoeda")
                }
            } else {
                quandoFalhaVenda("Valor inválido")
            }
        }
    }
}