package br.com.brq.agatha.investimentos.ui.recyclerview

import android.content.Context
import br.com.brq.agatha.investimentos.model.Moeda
import junit.framework.TestCase
import org.mockito.Mockito
import java.math.BigDecimal

class ListaMoedasAdpterTest : TestCase() {

    private val moedaDeExemplo = Moeda(
        name = "teste",
        buy = BigDecimal.ZERO,
        sell = BigDecimal.ZERO,
        abreviacao = "teste",
        totalDeMoeda = 0.00,
        variation = BigDecimal.ZERO
    )

    fun testGetQuandoMoedaClicado() {}

    fun testSetQuandoMoedaClicado() {}

    fun testOnCreateViewHolder() {}

    fun testOnBindViewHolder() {
    }

    fun testGetItemCount() {
        val contex = Mockito.mock(Context::class.java)
        val listaMoedasAdpter = Mockito.spy(ListaMoedasAdpter(contex))
        assertEquals(0, listaMoedasAdpter.itemCount)
    }

    fun testAtualiza() {}

    fun testAdiciona() {
        // Mock Não executa nada do obejeto que estamos mokando, diferente do spy
        val contex = Mockito.mock(Context::class.java)

        // Objeto espião Syp - Executa as funções do ojeto mas tem a capaciadae de mudar seus comportamentos
        val listaMoedasAdpter = Mockito.spy(ListaMoedasAdpter(contex))
        Mockito.doNothing().`when`(listaMoedasAdpter).atualizaAoAdicionar()

        listaMoedasAdpter.adiciona(moedaDeExemplo)
        assertEquals(1, listaMoedasAdpter.itemCount)

    }

    fun testAtualizaAoAdicionar() {}


}