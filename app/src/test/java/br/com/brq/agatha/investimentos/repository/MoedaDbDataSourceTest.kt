package br.com.brq.agatha.investimentos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.investimentos.database.dao.MoedaDao
import br.com.brq.agatha.domain.model.Moeda
import br.com.brq.agatha.investimentos.viewModel.base.TestContextProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class MoedaDbDataSourceTest {
    @get : Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var moedaDao: MoedaDao

    private lateinit var moedaDbDataSource: MoedaDbDataSource

    private val moedaDeExemplo = br.com.brq.agatha.domain.model.Moeda(
        name = "Dollar",
        buy = BigDecimal.TEN,
        sell = BigDecimal.TEN,
        abreviacao = "USD",
        totalDeMoeda = 10.00,
        variation = BigDecimal(1)
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        moedaDbDataSource = MoedaDbDataSource(moedaDao, TestContextProvider())
    }

    @Test
    fun deveRetornarUmLiveDateComOTotalDeMoedas_quandoChamaFuncaoGetTotalMoeda() {
        coEvery { moedaDao.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        assertEquals(10.00, moedaDbDataSource.getTotalMoeda(moedaDeExemplo.name).value)
    }

    @Test
    fun deveSetarOTotalDeMoeda_quandoChamaAFuncaoSetTotalMoedaAposCompra(){
        coEvery { moedaDao.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        moedaDbDataSource.setTotalMoedaAposCompra(moedaDeExemplo.name, 5.00)
        assertEquals(15.00, moedaDeExemplo.totalDeMoeda)
    }

    @Test
    fun deveSetarOTotalDeMoeda_quandoChamaAFuncaoSetTotalMoedaAposVenda(){
        coEvery { moedaDao.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        moedaDbDataSource.setTotalMoedaAposVenda(moedaDeExemplo.name, 5.00)
        assertEquals(5.00, moedaDeExemplo.totalDeMoeda)
    }


}