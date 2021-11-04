package br.com.brq.agatha.base.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brq.agatha.base.database.dao.MoedaDao
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
        totalDeMoeda = 10,
        variation = BigDecimal(1)
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        moedaDbDataSource = MoedaDbDataSource(moedaDao)
    }


    @Test
    fun deveSetarOTotalDeMoeda_quandoChamaAFuncaoSetTotalMoedaAposCompra(){
        coEvery { moedaDao.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        coEvery {  moedaDbDataSource.setTotalMoedaAposCompra(moedaDeExemplo.name, 5) }
        assertEquals(15.00, moedaDeExemplo.totalDeMoeda)
    }

    @Test
    fun deveSetarOTotalDeMoeda_quandoChamaAFuncaoSetTotalMoedaAposVenda(){
        coEvery { moedaDao.buscaMoeda(moedaDeExemplo.name)} returns moedaDeExemplo
        coEvery { moedaDbDataSource.setTotalMoedaAposVenda(moedaDeExemplo.name, 5) }
        assertEquals(5.00, moedaDeExemplo.totalDeMoeda)
    }


}