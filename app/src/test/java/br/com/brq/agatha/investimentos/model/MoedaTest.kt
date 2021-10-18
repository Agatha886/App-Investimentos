package br.com.brq.agatha.investimentos.model

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.brq.agatha.investimentos.R
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import java.math.BigDecimal


class MoedaTest : TestCase() {

    private val moedaDeExemplo = Moeda(
        name = "Dollar",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "USD",
        totalDeMoeda = 0.00,
        variation = BigDecimal(-1)
    )

    private val moedaDeExemplo2 = Moeda(
        name = "Bitcoin",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "BTC",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )
    private val moedaDeExemplo3 = Moeda(
        name = "Euro",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "EUR",
        totalDeMoeda = 0.00,
        variation = BigDecimal(10)
    )

    fun testSetAbreviacao() {
        assertEquals("USD", moedaDeExemplo.setAbreviacao())
    }

    fun testRetornaCor() {
        val context: Context = mockk<Context>()
        val colorRed: Int = -1
        val colorWhite: Int = -3145189
        val colorVerde = -8465631

        every { ContextCompat.getColor(context, R.color.red) } returns colorRed
        every { ContextCompat.getColor(context, R.color.white) } returns colorWhite
        every { ContextCompat.getColor(context, R.color.verde) } returns colorVerde

        assertEquals(colorRed, moedaDeExemplo.retornaCor(context))
        assertEquals(colorWhite, moedaDeExemplo2.retornaCor(context))
        assertEquals(colorVerde, moedaDeExemplo3.retornaCor(context))
    }

    fun testSetTotalMoedaCompra() {
        moedaDeExemplo.setTotalMoedaCompra(10.00)
        assertEquals(10.00, moedaDeExemplo.totalDeMoeda)

        moedaDeExemplo.setTotalMoedaCompra(-1.00)
        assertEquals(10.00, moedaDeExemplo.totalDeMoeda)

        moedaDeExemplo.setTotalMoedaCompra(0.00)
        assertEquals(10.00, moedaDeExemplo.totalDeMoeda)
    }

    fun testSetTotalMoedaVenda() {
        moedaDeExemplo.setTotalMoedaVenda(11.00)
        assertEquals(11.00, moedaDeExemplo.totalDeMoeda)

        moedaDeExemplo.setTotalMoedaVenda(0.00)
        assertEquals(0.00, moedaDeExemplo.totalDeMoeda)

        moedaDeExemplo.setTotalMoedaVenda(-1.00)
        assertEquals(0.00, moedaDeExemplo.totalDeMoeda)
    }

    fun testSetMoedaSimbulo() {
        assertEquals("US$ 10,00", moedaDeExemplo.setMoedaSimbulo(BigDecimal(10.00)))
        assertEquals("₿ 10,00", moedaDeExemplo2.setMoedaSimbulo(BigDecimal(10.00)))
        assertEquals("€ 10,00", moedaDeExemplo3.setMoedaSimbulo(BigDecimal(10.00)))
    }
}