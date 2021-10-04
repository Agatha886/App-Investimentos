package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import br.com.brq.agatha.investimentos.model.Moeda
import junit.framework.TestCase
import org.mockito.Mockito
import java.math.BigDecimal

class MoedaViewModelTest : TestCase() {

    private val moedaDeExemplo = Moeda(
        name = "Dollar",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "USD",
        totalDeMoeda = 0.00,
        variation = BigDecimal(-1)
    )
    val moedaViewModel = MoedaViewModel(Mockito.mock(Context::class.java))

    fun testGetTotalMoeda() {
        val liveDateTotalMoeda = moedaViewModel.getTotalMoeda("Dollar")
        assertEquals(0.00, liveDateTotalMoeda.value)
    }

    // Como consigo fazer um teste que verifica se chamou uma função

    fun testSetToltalMoedaCompra() {}

    fun testSetTotalMoedaVenda() {}

    fun testValidaTotalMoedaVenda() {
        moedaViewModel.validaTotalMoedaVenda("Dollar", "10")
        var erro: String = "Erro sem valor"
        moedaViewModel.quandoVendaFalha ={erro = it}
        assertEquals("Valor inválido", erro )

        var total: Double = 0.00
        moedaViewModel.quandoVendaSucesso = {totalDeMoeda -> total += totalDeMoeda }
        assertEquals(10.00, total)


    }
}