package br.com.brq.agatha.data.api

import br.com.brq.agatha.data.database.dao.UsuarioDao
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.domain.model.Usuario
import java.math.BigDecimal

class UsuarioRepository(
    private val daoUsuario: UsuarioDao,
) {

    fun getUsuario(idUsuario: Int): Usuario {
        return daoUsuario.retornaUsuario(idUsuario)
    }


    suspend fun adicionaUsuario(usuario: Usuario) {
        daoUsuario.adiciona(usuario)
    }

    fun modificaUsuario(usuario: Usuario) {
        daoUsuario.modifica(usuario)
    }

    fun calculaSaldoCompra(moeda: Moeda, valorComprado: String, user: Usuario): BigDecimal {
        val valorDaCompra = BigDecimal(valorComprado).multiply(moeda.buy)
        return user.saldoDisponivel.subtract(valorDaCompra)
    }

    fun calculaSaldoVenda(
        moeda: Moeda,
        valorVendaMoeda: String,
        user: Usuario
    ): BigDecimal {
        return user.saldoDisponivel.add(BigDecimal(valorVendaMoeda).multiply(moeda.sell))
    }

    fun calculaTotalDeMoedasVenda(moeda: Moeda, quantidadeParaVenda: String): Int {
        return moeda.totalDeMoeda.minus(BigDecimal(quantidadeParaVenda).toInt())
    }


}