package br.com.brq.agatha.investimentos.model

import junit.framework.TestCase
import java.math.BigDecimal

class UsuarioTest : TestCase() {

    private val moedaDeExemplo = Moeda(
        name = "Dollar",
        buy = BigDecimal(5),
        sell = BigDecimal(2),
        abreviacao = "USD",
        totalDeMoeda = 0.00,
        variation = BigDecimal(-1)
    )

    private val usuarioDeExemplo = Usuario(saldoDisponivel = BigDecimal.ZERO)

    fun testSetSaldo() {
        usuarioDeExemplo.setSaldo(BigDecimal(1000))
        assertEquals(BigDecimal(1000), usuarioDeExemplo.saldoDisponivel)
    }

    fun testCalculaSaldoVenda() {
        val calculaSaldoVenda = usuarioDeExemplo.calculaSaldoVenda(moedaDeExemplo, "5000045698258963")
        assertEquals(BigDecimal(10000091396517926), calculaSaldoVenda)
    }
}